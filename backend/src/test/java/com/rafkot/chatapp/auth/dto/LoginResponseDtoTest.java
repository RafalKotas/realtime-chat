package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String userName = "test-user-name";
        String userId = "test-user-id";
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";


        // when
        LoginResponseDto loginResponseDto = new LoginResponseDto(
                userName, userId, accessToken, refreshToken
        );

        // then
        assertThat(loginResponseDto).isNotNull();
        assertThat(loginResponseDto.username()).isEqualTo("test-user-name");
        assertThat(loginResponseDto.userId()).isEqualTo("test-user-id");
        assertThat(loginResponseDto.accessToken()).isEqualTo("test-access-token");
        assertThat(loginResponseDto.refreshToken()).isEqualTo("test-refresh-token");
    }
}