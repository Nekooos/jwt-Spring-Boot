package com.practice.jwtapp.exception;

public class EmailExistsException extends RuntimeException{

    private String message;

    public EmailExistsException(String message) {
        this.message = message;
    }
}
