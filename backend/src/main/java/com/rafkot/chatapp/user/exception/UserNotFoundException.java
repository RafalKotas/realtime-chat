package com.rafkot.chatapp.user.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    private final UUID userId;
    private final HttpStatus httpStatus;
    private final Map<String, String> errors;

    public UserNotFoundException(UUID userId) {
        super("User with id %s not found".formatted(userId));
        this.userId = userId;
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.errors = Map.of("userId", "User not found");
    }
}
