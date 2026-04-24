package com.rafkot.chatapp.security.auth.dto;

public record AuthenticationRequestDto (
    String username,
    String password
) {
}
