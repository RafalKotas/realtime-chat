package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(authenticationRequestDto);
        assertEquals("exampleUsername", authenticationRequestDto.login());
        assertEquals("superStrongPassword", authenticationRequestDto.password());
    }
}