package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class AuthenticationRequestDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String login = "exampleUsername";
        String password = "superStrongPassword";

        // when
        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
                login,
                password
        );

        // then
        assertThat(authenticationRequestDto).isNotNull();
        assertThat(authenticationRequestDto.login()).isEqualTo("exampleUsername");
        assertThat(authenticationRequestDto.password()).isEqualTo("superStrongPassword");
    }
}