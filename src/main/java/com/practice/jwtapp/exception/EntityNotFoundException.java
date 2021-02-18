package com.practice.jwtapp.exception;

public class EntityNotFoundException extends RuntimeException {
    private String message;

    public EntityNotFoundException(String message) {
        this.message = message;
    }
}
