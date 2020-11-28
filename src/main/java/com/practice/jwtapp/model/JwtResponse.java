package com.practice.jwtapp.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private final String jwtToken;

    public JwtResponse(String jwttoken) {
        this.jwtToken = jwttoken;
    }

    public String getToken() {
        return this.jwtToken;
    }
}

