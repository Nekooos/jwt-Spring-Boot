package com.practice.jwtapp.config;

import com.practice.jwtapp.service.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class JwtTokenUtilTest {
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private JwtUserDetailsService userDetailsService;
    private String testToken;
    private final String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZWZhdWx0VXNlciIsImV4cCI6MTYwMTY4MTkzOSwiaWF0IjoxNjAxNjYzOTM5fQ.x8pSmsjcaBLEPehZXg3qc5TAB5ETsLUMX6ZSpoYqzBKAf10bpdrTitsw64dnvRpjkgNyk_zdcVtnssX4JGBilA";

    @BeforeEach
    public void setup() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "testSecret");
        userDetailsService = new JwtUserDetailsService();
        testToken = createJwtToken();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateToken() {
        Mockito.when(userDetailsService.loadUserByUsername("defaultUser"))
                .thenReturn(new org.springframework.security.core.userdetails.User("defaultUser", "password", new ArrayList<>()));
        Boolean isValidToken = jwtTokenUtil.validateToken(testToken, userDetailsService.loadUserByUsername("defaultUser"));
        Assertions.assertEquals(true, isValidToken);
    }

    @Test
    public void getAExpirationExpiredJwtException() {
        Assertions.assertThrows(ExpiredJwtException.class, () ->
                jwtTokenUtil.getClaimFromToken(expiredToken, Claims::getExpiration));
    }

    @Test
    public void getSubject() {
        String subject = jwtTokenUtil.getClaimFromToken(testToken, Claims::getSubject);
        Assertions.assertEquals(subject, "defaultUser");
    }

    @Test
    public void generateToken() {
        //Set<GrantedAuthority> roles = createRoles();
        Mockito.when(userDetailsService.loadUserByUsername("defaultUser"))
                .thenReturn(new org.springframework.security.core.userdetails.User("defaultUser", "password", new ArrayList<>()));
        String token = jwtTokenUtil.generateToken(userDetailsService.loadUserByUsername("defaultUser"));
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        Assertions.assertEquals("defaultUser", userName);
    }

    @Test
    public void getCustomClaimFromToken() {
        Object role = jwtTokenUtil.getCustomClaimFromToken(testToken, "Role");
        Assertions.assertEquals(role, "Admin");
    }

    private String createJwtToken() {
        return Jwts.builder()
                .setClaims(addClaims())
                .setSubject("defaultUser")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();
    }

    private Map<String, Object> addClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Role", "Admin");
        claims.put("Group", "TestGroup");
        return claims;
    }
}
