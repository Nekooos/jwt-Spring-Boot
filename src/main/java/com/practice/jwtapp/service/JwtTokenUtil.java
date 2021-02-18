package com.practice.jwtapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.refresh}")
    private long refresh;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.authorities}")
    private String authorities;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Object getCustomClaimFromToken(String token, String customClaim) {
        return getClaimFromToken(token, claims -> claims.get(customClaim)) ;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claims) {
        return claims.apply(getAllClaimsFromToken(token));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .claim(authorities, getAuthorities(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(DefaultClaims claims) {
        return Jwts.builder()
                .claim(authorities, claims.get(authorities))
                .setSubject(claims.getSubject())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refresh))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private String getAuthorities(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getJwtTokenFromRequest(HttpServletRequest request) {
        String header = Optional.ofNullable(request.getHeader("Authorization")).orElse("");
        return header.contains("Bearer ") ? header.substring(7) : "";
    }
}
