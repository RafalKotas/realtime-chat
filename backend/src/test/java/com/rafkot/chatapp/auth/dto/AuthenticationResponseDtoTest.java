package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthenticationResponseDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String token = "Xz8Wlp352lkxmz1";

        // when
        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto(
                token
        );

        // then
        assertNotNull(authenticationResponseDto);
        assertEquals("Xz8Wlp352lkxmz1", authenticationResponseDto.token());
    }
}