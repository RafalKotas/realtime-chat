package com.rafkot.chatapp.security.auth.dto;

public record RegistrationRequestDto(
        String username,
        String email,
        String password
) {
}
