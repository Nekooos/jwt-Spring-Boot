package com.practice.jwtapp.exception;

import com.practice.jwtapp.model.ErrorResponseDto;
import com.practice.jwtapp.model.FieldErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class JsonExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, UsernameExistsException.class})
    @ResponseBody
    public ResponseEntity<Object> handleForm(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        List<FieldErrorDto> fieldErrors = processFieldErrors(bindingResult.getFieldErrors());

        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponseDto("Form validation failed", fieldErrors));
    }

    @ExceptionHandler({MailException.class})
    @ResponseBody
    public ResponseEntity<Object> UserNotFound(MailException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponseDto("Could not send email"));
    }

    @ExceptionHandler({PasswordResetTokenNotValidException.class})
    @ResponseBody
    public ResponseEntity<Object> passwordResetTokenNotValid(MailException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponseDto("Url is not valid or expired"));
    }

    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class, PasswordResetTokenNotFoundException.class})
    @ResponseBody
    public ResponseEntity<Object> UserNotFound(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleAllOtherErrors(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    private List<FieldErrorDto> processFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fieldError -> new FieldErrorDto(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    }

}
