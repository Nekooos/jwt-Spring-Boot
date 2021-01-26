package com.practice.jwtapp.model;

import java.util.List;

public class ErrorResponseDto {
    private String message;
    private List<FieldErrorDto> fieldErrors;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    public ErrorResponseDto(String message, List<FieldErrorDto> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }
}
