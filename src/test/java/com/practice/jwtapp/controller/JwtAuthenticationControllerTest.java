package com.practice.jwtapp.controller;

import com.practice.jwtapp.config.JwtTokenUtil;
import com.practice.jwtapp.model.JwtRequest;
import com.practice.jwtapp.model.JwtResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtAuthenticationControllerTest {

    private JwtAuthenticationController jwtAuthenticationController;
    private JwtRequest jwtRequest;
    private JwtTokenUtil jwtTokenUtil;
    private String token;

    @Mock
    AuthenticationManager authenticationManager;

    @BeforeAll
    public void setup() {
        jwtAuthenticationController = new JwtAuthenticationController();
        jwtRequest = new JwtRequest("password", "defaultUser");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAuthenticationToken() throws Exception {
        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())));

        ResponseEntity<?> responseEntity = jwtAuthenticationController.createAuthenticationToken(jwtRequest);
        Assertions.assertEquals(responseEntity,  ResponseEntity.ok(new JwtResponse(token)));
    }
}
