package com.rafkot.chatapp.user.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("User with id %s not found".formatted(userId));
    }
}
