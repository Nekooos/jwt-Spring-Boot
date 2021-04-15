package com.practice.jwtapp.model;

import java.util.List;

public class ErrorResponse {
    private String message;
    private List<FieldError> fieldErrors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, List<FieldError> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
