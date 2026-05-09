package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(registrationResponseDto);
        assertEquals("exampleUsername", registrationResponseDto.username());
        assertEquals("exampleUserEmail@mail.com", registrationResponseDto.email());
    }
}