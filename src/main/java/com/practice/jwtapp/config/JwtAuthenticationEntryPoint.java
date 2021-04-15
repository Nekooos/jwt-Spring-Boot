package com.practice.jwtapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.jwtapp.model.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, createErrorResponse(response));
    }

    private String createErrorResponse(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("Invalid username or password");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(errorResponse);
    }
}
