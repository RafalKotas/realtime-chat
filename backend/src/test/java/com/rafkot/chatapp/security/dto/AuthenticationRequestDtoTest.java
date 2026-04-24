package com.rafkot.chatapp.security.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationRequestDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String username = "exampleUsername";
        String password = "superStrongPassword";

        // when
        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
                username,
                password
        );

        // then
        assertNotNull(authenticationRequestDto);
        assertEquals("exampleUsername", authenticationRequestDto.username());
        assertEquals("superStrongPassword", authenticationRequestDto.password());
    }
}