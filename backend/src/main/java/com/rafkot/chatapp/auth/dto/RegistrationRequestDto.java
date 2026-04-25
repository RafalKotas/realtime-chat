package com.rafkot.chatapp.auth.dto;

public record RegistrationRequestDto(
        String username,
        String email,
        String password
) {
}
