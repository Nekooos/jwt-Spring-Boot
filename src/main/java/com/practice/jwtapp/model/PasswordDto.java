package com.practice.jwtapp.model;

import javax.validation.constraints.NotNull;

public class PasswordDto {
    @NotNull(message = "url is expired or missing")
    private  String token;

    @NotNull(message = "New password is required")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
