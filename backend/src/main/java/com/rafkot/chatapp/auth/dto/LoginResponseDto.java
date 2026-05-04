package com.rafkot.chatapp.auth.dto;

public record LoginResponseDto(
        String username,
        String userId,
        String accessToken,
        String refreshToken
) {
}
