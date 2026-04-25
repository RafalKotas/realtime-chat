package com.rafkot.chatapp.auth.dto;

import com.rafkot.chatapp.auth.dto.RegistrationRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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