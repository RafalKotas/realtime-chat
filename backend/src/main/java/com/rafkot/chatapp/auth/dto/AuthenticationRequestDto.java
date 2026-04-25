package com.rafkot.chatapp.auth.dto;

public record AuthenticationRequestDto (
    String username,
    String password
) {
}
