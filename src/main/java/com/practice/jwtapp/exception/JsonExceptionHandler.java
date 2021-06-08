package com.practice.jwtapp.exception;

import com.practice.jwtapp.model.ErrorResponse;
import com.practice.jwtapp.model.FieldError;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class JsonExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseBody
    public ResponseEntity<Object> handleForm(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        List<FieldError> fieldErrors = processFieldErrors(bindingResult.getFieldErrors());

        return ResponseEntity.status(CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse("Form validation failed", fieldErrors));
    }


    @ExceptionHandler({ExpiredJwtException.class})
    @ResponseBody
    public ResponseEntity<Object> expiredJwtException() {
        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse("Expired"));
    }

    @ExceptionHandler({EmailExistsException.class})
    @ResponseBody
    public ResponseEntity<Object> emailExists() {
        return ResponseEntity.status(CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse("Email is already taken"));
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseBody
    public ResponseEntity<Object> badCredentials() {
        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse("Invalid username or password"));
    }

    //NotAuthorizedException
    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<Object> authenticationException(AuthenticationException authenticationException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(authenticationException.getMessage()));
    }

    @ExceptionHandler({MailException.class})
    @ResponseBody
    public ResponseEntity<Object> mailError(MailException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse("Could not send email"));
    }

    @ExceptionHandler({AccountTokenNotValidException.class})
    @ResponseBody
    public ResponseEntity<Object> passwordResetTokenNotValid(MailException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse("Url is not valid or expired"));
    }

    @ExceptionHandler({UserNotFoundException.class, AccountTokenNotFoundException.class})
    @ResponseBody
    public ResponseEntity<Object> UserNotFound(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleAllOtherErrors(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> UnauthorizedException(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(exception.getMessage()));
    }

    private List<FieldError> processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fieldError -> new FieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}
