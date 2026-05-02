package com.rafkot.chatapp.common;

import com.rafkot.chatapp.user.exception.UserValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUserValidation(UserValidationException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(exception.getErrors());
    }
}
