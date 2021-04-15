package com.practice.jwtapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserDto {

    @NotNull(message = "Email is required")
    @Email(message = "Not a valid email address")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    public UserDto() {}

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
