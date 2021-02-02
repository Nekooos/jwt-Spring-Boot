package com.practice.jwtapp.exception;

public class AccountTokenNotFoundException extends RuntimeException {
    private String message;

    public AccountTokenNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
