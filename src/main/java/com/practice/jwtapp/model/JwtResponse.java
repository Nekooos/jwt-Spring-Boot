package com.practice.jwtapp.model;

public class JwtResponse {
    private final String jwtToken;
    private String refreshJwtToken;

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public JwtResponse(String jwtToken, String refreshJwtToken) {
        this.jwtToken = jwtToken;
        this.refreshJwtToken = refreshJwtToken;
    }

    public String getToken() {
        return this.jwtToken;
    }

    public String getRefreshJwtToken() {
        return refreshJwtToken;
    }
}

