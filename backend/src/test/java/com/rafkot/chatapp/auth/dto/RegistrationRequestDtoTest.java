package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(registrationRequestDto);
        assertEquals("exampleUsername", registrationRequestDto.username());
        assertEquals("exampleUserEmail@mail.com", registrationRequestDto.email());
        assertEquals("superStrongPassword", registrationRequestDto.password());
    }
}