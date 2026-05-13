package com.rafkot.chatapp.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("User with name %s not found".formatted(username));
    }
}
