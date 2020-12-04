package com.practice.jwtapp.controller;

import com.practice.jwtapp.config.JwtTokenUtil;
import com.practice.jwtapp.model.JwtRequest;
import com.practice.jwtapp.model.JwtResponse;
import com.practice.jwtapp.service.JwtUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.mockito.Mockito.times;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtAuthenticationControllerTest {
    @InjectMocks
    private JwtAuthenticationController jwtAuthenticationController;

    private JwtRequest jwtRequest;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUserDetailsService userDetailsService;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @BeforeAll
    public void setup() {

        jwtAuthenticationController = new JwtAuthenticationController();
        jwtRequest = new JwtRequest("password", "defaultUser");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAuthenticationToken() throws Exception {
        Mockito.when(userDetailsService.loadUserByUsername(jwtRequest.getUsername()))
                .thenReturn(createUser(jwtRequest));

        ResponseEntity<?> responseEntity = jwtAuthenticationController.createAuthenticationToken(jwtRequest);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Mockito.verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        Mockito.verify(userDetailsService, times(1))
                .loadUserByUsername(jwtRequest.getUsername());
        Mockito.verify(jwtTokenUtil,times(1))
                .generateToken(createUser(jwtRequest));
    }

    private User createUser(JwtRequest jwtRequest) {
        return new User(jwtRequest.getUsername(), jwtRequest.getPassword(), Collections.emptySet());
    }
}
