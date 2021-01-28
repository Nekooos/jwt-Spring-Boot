package com.practice.jwtapp.exception;

public class PasswordResetTokenNotFoundException extends RuntimeException {
    private String message;

    public PasswordResetTokenNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
