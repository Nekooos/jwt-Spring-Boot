package com.practice.jwtapp.controller;

import com.practice.jwtapp.config.JwtTokenUtil;
import com.practice.jwtapp.model.JwtRequest;
import com.practice.jwtapp.model.JwtResponse;
import com.practice.jwtapp.service.JwtUserDetailsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User does not exist");
        } catch (Exception e) {
            //
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
