package com.practice.jwtapp.exception;

public class AccountTokenNotValidException extends RuntimeException {
    private String message;

    public AccountTokenNotValidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
