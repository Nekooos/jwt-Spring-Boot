package com.practice.jwtapp.config;

import com.practice.jwtapp.service.JwtTokenUtil;
import com.practice.jwtapp.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtToken = jwtTokenUtil.getJwtTokenFromRequest(request);

        try {
            if (!jwtToken.isEmpty()) {
                final String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    authenticate(userDetails, request);
                }
            }
        } catch (ExpiredJwtException expiredJwtException) {
            String refreshToken = Optional.of(request.getHeader("refreshToken")).orElse("");
            String requestURL = request.getRequestURL().toString();
            System.out.println(requestURL);

            //if (!refreshToken.isEmpty() && refreshToken.equals("true") && requestURL.contains("refresh-token")) {
            if (!refreshToken.isEmpty() && jwtTokenUtil.validateToken(refreshToken, expiredJwtException)) {
                refreshToken(expiredJwtException, request);
            } else {
                request.setAttribute("exception", expiredJwtException);
            }

        } catch (SignatureException e) {
            //
        } catch (IllegalArgumentException e) {
            //
        } catch (MalformedJwtException e) {
            //
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private void refreshToken(ExpiredJwtException expiredJwtException, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.setAttribute("claims", expiredJwtException.getClaims());
    }

}