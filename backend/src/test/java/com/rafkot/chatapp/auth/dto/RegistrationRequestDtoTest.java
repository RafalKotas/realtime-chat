package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class RegistrationRequestDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String username = "exampleUsername";
        String email = "exampleUserEmail@mail.com";
        String password = "superStrongPassword";

        // when
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
                username,
                email,
                password
        );

        // then
        assertThat(registrationRequestDto).isNotNull();
        assertThat(registrationRequestDto.username()).isEqualTo("exampleUsername");
        assertThat(registrationRequestDto.email()).isEqualTo("exampleUserEmail@mail.com");
        assertThat(registrationRequestDto.password()).isEqualTo("superStrongPassword");
    }
}