package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChangePasswordRequestDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String password = "newPassword";
        String confirmPassword = "newPassword";

        // when
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(
                password, confirmPassword
        );

        // then
        assertThat(changePasswordRequestDto).isNotNull();
        assertThat(changePasswordRequestDto.password()).isEqualTo("newPassword");
        assertThat(changePasswordRequestDto.confirmPassword()).isEqualTo("newPassword");
    }
}