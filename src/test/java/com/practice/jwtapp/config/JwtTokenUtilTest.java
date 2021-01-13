package com.practice.jwtapp.config;

import com.practice.jwtapp.service.JwtUserDetailsService;
import com.practice.jwtapp.testUtil.TestUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtTokenUtilTest {
    private TestUtil testUtil;
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private JwtUserDetailsService userDetailsService;
    private String testToken;
    private final String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZWZhdWx0VXNlciIsImV4cCI6MTYwMTY4MTkzOSwiaWF0IjoxNjAxNjYzOTM5fQ.x8pSmsjcaBLEPehZXg3qc5TAB5ETsLUMX6ZSpoYqzBKAf10bpdrTitsw64dnvRpjkgNyk_zdcVtnssX4JGBilA";

    @BeforeEach
    public void setup() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "testSecret");
        ReflectionTestUtils.setField(jwtTokenUtil, "authorities", "testAuthorities");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 180000000);
        ReflectionTestUtils.setField(jwtTokenUtil, "refresh", 180000000);
        userDetailsService = new JwtUserDetailsService();
        testUtil = new TestUtil();
        testToken = createJwtToken();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateToken() {
        when(userDetailsService.loadUserByUsername("defaultUser"))
                .thenReturn(new org.springframework.security.core.userdetails.User("defaultUser", "password", new ArrayList<>()));
        Boolean isValidToken = jwtTokenUtil.validateToken(testToken, userDetailsService.loadUserByUsername("defaultUser"));
        assertTrue(isValidToken);
    }

    @Test
    public void getAExpirationExpiredJwtException() {
        assertThrows(ExpiredJwtException.class, () ->
                jwtTokenUtil.getClaimFromToken(expiredToken, Claims::getExpiration));
    }

    @Test
    public void getSubject() {
        String subject = jwtTokenUtil.getClaimFromToken(testToken, Claims::getSubject);
        assertEquals(subject, "defaultUser");
    }

    @Test
    public void generateToken() {
        when(userDetailsService.loadUserByUsername("defaultUser"))
                .thenReturn(new org.springframework.security.core.userdetails.User("defaultUser", "password", testUtil.createAuthorities()));
        String token = jwtTokenUtil.generateToken(userDetailsService.loadUserByUsername("defaultUser"));
        String username = jwtTokenUtil.getUsernameFromToken(token);
        String roles = (String)jwtTokenUtil.getCustomClaimFromToken(token, "testAuthorities");
        assertEquals("defaultUser", username);
        assertEquals("ROLE_ADMIN,ROLE_EDITOR,ROLE_USER", roles);
    }

    @Test
    public void generateRefreshToken() {
        DefaultClaims claims = new DefaultClaims();
        claims.setSubject("defaultUser");
        claims.put("testAuthorities", "ROLE_USER,ROLE_EDITOR,ROLE_ADMIN");

        String refreshToken = jwtTokenUtil.generateRefreshToken(claims);

        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
        String roles = (String)jwtTokenUtil.getCustomClaimFromToken(refreshToken, "testAuthorities");
        assertEquals("defaultUser", username);
        assertEquals("ROLE_USER,ROLE_EDITOR,ROLE_ADMIN", roles);
    }

    @Test
    public void getCustomClaimFromToken() {
        String customClaim = (String)jwtTokenUtil.getCustomClaimFromToken(testToken, "customClaim");
        assertEquals("testClaim", customClaim);
    }

    private String createJwtToken() {
        return Jwts.builder()
                .claim("authorities", testUtil.createAuthorities())
                .claim("customClaim", "testClaim")
                .setSubject("defaultUser")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();
    }
}
