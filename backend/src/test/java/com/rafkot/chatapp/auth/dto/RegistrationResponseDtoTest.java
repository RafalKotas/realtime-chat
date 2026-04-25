package com.rafkot.chatapp.auth.dto;

import com.rafkot.chatapp.auth.dto.RegistrationResponseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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