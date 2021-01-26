package com.practice.jwtapp.exception;

public class UsernameExistsException extends RuntimeException{

    private String message;

    public UsernameExistsException(String message) {
        this.message = message;
    }
}
