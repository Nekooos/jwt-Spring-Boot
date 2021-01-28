package com.practice.jwtapp.exception;

public class PasswordResetTokenNotValidException extends RuntimeException {
    private String message;

    public PasswordResetTokenNotValidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
