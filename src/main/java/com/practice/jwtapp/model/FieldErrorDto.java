package com.practice.jwtapp.model;

public class FieldErrorDto {
    private final String objectName;
    private final String field;
    private final String fieldAnnotation;
    private final String errorMessage;

    public FieldErrorDto(String objectName, String field, String fieldAnnotation, String errorMessage) {
        this.objectName = objectName;
        this.field = field;
        this.fieldAnnotation = fieldAnnotation;
        this.errorMessage = errorMessage;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getFieldAnnotation() { return fieldAnnotation; }
}
