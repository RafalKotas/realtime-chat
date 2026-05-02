package com.rafkot.chatapp.auth.dto;

public record LoginResponseDto(
        String username,
        String accessToken,
        String refreshToken
) {
}
