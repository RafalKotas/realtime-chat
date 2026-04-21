package com.rafkot.chatapp.user.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class UserValidationException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Map<String, String> errors;

    public UserValidationException(HttpStatus httpStatus, Map<String, String> errors)
    {
        super("User validation failed");
        this.httpStatus = httpStatus;
        this.errors = errors;
    }
}
