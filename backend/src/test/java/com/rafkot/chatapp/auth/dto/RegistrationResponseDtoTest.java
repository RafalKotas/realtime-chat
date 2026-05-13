package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class RegistrationResponseDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String username = "exampleUsername";
        String email = "exampleUserEmail@mail.com";

        // when
        RegistrationResponseDto registrationResponseDto = new RegistrationResponseDto(
                username,
                email
        );

        // then
        assertThat(registrationResponseDto).isNotNull();
        assertThat(registrationResponseDto.username()).isEqualTo("exampleUsername");
        assertThat(registrationResponseDto.email()).isEqualTo("exampleUserEmail@mail.com");
    }
}