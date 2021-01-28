package com.practice.jwtapp.model;

import javax.validation.constraints.NotNull;

public class PasswordDto {
    @NotNull(message = "password is required")
    private String oldPassword;
    @NotNull(message = "url is expired or missing")
    private  String token;

    @NotNull(message = "New password is required")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

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
