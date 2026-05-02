package com.rafkot.chatapp.auth.dto;

public record AuthenticationRequestDto (
    String login,
    String password
) {
}
