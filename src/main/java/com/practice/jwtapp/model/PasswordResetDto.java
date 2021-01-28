package com.practice.jwtapp.model;

import javax.validation.constraints.NotNull;

public class PasswordResetDto {
    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Confirm password is required")
    private String confirmPassword;

    @NotNull(message = "Token is missing from password reset")
    private String token;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
