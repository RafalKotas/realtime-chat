package com.rafkot.chatapp.security.dto;

public record RegistrationRequestDto(
        String username,
        String email,
        String password
) {
}
