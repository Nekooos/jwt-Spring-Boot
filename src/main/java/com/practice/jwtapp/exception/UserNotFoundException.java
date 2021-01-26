package com.practice.jwtapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private String message;

    public UserNotFoundException(String message, String message1) {
        this.message = message1;
    }
}
