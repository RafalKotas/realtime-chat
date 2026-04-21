package com.rafkot.chatapp.security.dto;

public record AuthenticationRequestDto (
    String username,
    String password
) {
}
