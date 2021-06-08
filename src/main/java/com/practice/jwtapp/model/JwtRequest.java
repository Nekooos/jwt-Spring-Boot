package com.practice.jwtapp.model;

import javax.validation.constraints.NotNull;

public class JwtRequest {
    @NotNull(message = "Username is required")
    private String email;
    @NotNull(message = "Password is required")
    private String password;


    public JwtRequest() {

    }

    public JwtRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
