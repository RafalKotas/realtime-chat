package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChangePasswordResponseDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String message = "Test response message";

        // when
        ChangePasswordResponseDto changePasswordResponseDto = new ChangePasswordResponseDto(
                message
        );

        // then
        assertThat(changePasswordResponseDto).isNotNull();
        assertThat(changePasswordResponseDto.message()).isEqualTo("Test response message");
    }
}