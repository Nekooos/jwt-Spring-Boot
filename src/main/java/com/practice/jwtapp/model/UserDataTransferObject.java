package com.practice.jwtapp.model;

public class UserDataTransferObject {
    private String username;
    private String password;

    public UserDataTransferObject() {}

    public UserDataTransferObject(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
