package com.rafkot.chatapp.auth.dto;

public record LoginResponseDto(
        String accessToken,
        String refreshToken
) {
}
