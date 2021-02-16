package com.practice.jwtapp.config;

import com.practice.jwtapp.service.JwtUserDetailsService;
import com.practice.jwtapp.testUtil.TestUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtRequestFilterTest {
    private MockHttpServletRequest httpServletRequest;
    private TestUtil testUtil;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    JwtRequestFilter jwtRequestFilter;
    @Mock
    HttpServletResponse httpServletResponse;
    @Mock
    FilterChain filterChain;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testUtil = new TestUtil();
        httpServletRequest = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("doFilterInternal")
    public void doFilterInternal() throws ServletException, IOException {
        String token = createJwtToken();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("user@mail.com", "password", new ArrayList<>());

        when(jwtTokenUtil.getJwtTokenFromRequest(httpServletRequest))
                .thenCallRealMethod();
        when(jwtTokenUtil.getUsernameFromToken(anyString()))
                .thenReturn("user@mail.com");
        when(jwtTokenUtil.validateToken(token, userDetails))
                .thenReturn(true);
        when(jwtUserDetailsService.loadUserByUsername("user@mail.com"))
                .thenReturn(userDetails);

        httpServletRequest.addHeader("Authorization", "Bearer " + token);

        jwtRequestFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(jwtTokenUtil).getJwtTokenFromRequest(httpServletRequest);
        verify(jwtTokenUtil).getUsernameFromToken(anyString());
        verify(jwtTokenUtil).validateToken(token, userDetails);
        verify(jwtUserDetailsService).loadUserByUsername("user@mail.com");
    }

    @Test
    @DisplayName("doFilterInternal throws ExpiredJwtException")
    public void doFilterInternalExpiredJwtException() throws ServletException, IOException {
        String token = createJwtToken();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("user@mail.com", "password", new ArrayList<>());

        when(jwtTokenUtil.getJwtTokenFromRequest(httpServletRequest))
                .thenCallRealMethod();
        when(jwtTokenUtil.getUsernameFromToken(anyString()))
                .thenReturn("user@mail.com");
        when(jwtTokenUtil.validateToken(token, userDetails))
                .thenThrow(ExpiredJwtException.class);
        when(jwtUserDetailsService.loadUserByUsername("user@mail.com"))
                .thenReturn(userDetails);

        httpServletRequest.addHeader("Authorization", "Bearer " + token);
        httpServletRequest.addHeader("refreshToken", "true");
        httpServletRequest.setRequestURI("refresh-token");

        jwtRequestFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(jwtTokenUtil).getJwtTokenFromRequest(httpServletRequest);
        verify(jwtTokenUtil).getUsernameFromToken(anyString());
        verify(jwtTokenUtil).validateToken(token, userDetails);
        verify(jwtUserDetailsService).loadUserByUsername("user@mail.com");
    }

    private String createJwtToken() {
        return Jwts.builder()
                .claim("authorities", testUtil.createAuthorities())
                .claim("customClaim", "testClaim")
                .setSubject("user@mail.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();
    }
}
