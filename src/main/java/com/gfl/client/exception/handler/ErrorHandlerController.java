package com.gfl.client.exception.handler;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        String errorMessage = ((ConstraintViolationException) ex).getConstraintViolations().stream()
                                        .map(violation -> violation.getMessage() + "; ")
                                        .collect(Collectors.joining("", "Validation error(s): ", ""));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult()
                                       .getAllErrors()
                                       .stream()
                                       .map(error -> error.getDefaultMessage() + "; ")
                                       .collect(Collectors.joining("", "Validation errors: ", ""));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations()
                                       .stream()
                                       .map(violation -> violation.getMessage() + "; ")
                                       .collect(Collectors.joining("", "Validation error(s): ", ""));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ResponseStatus(value = CONFLICT, reason = "Data integrity violation")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({Exception.class, IllegalStateException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler({RuntimeException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<String> handleAllUncaughtException(
            RuntimeException exception) {
        return ResponseEntity.status(METHOD_NOT_ALLOWED).body(exception.getMessage());
    }
}
