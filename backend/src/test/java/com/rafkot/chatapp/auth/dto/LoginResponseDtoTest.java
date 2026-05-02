package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoginResponseDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        String userName = "test-user-name";

        // when
        LoginResponseDto loginResponseDto = new LoginResponseDto(
                userName, accessToken, refreshToken
        );

        // then
        assertNotNull(loginResponseDto);
        assertEquals("test-user-name", loginResponseDto.username());
        assertEquals("test-access-token", loginResponseDto.accessToken());
        assertEquals("test-refresh-token", loginResponseDto.refreshToken());
    }
}