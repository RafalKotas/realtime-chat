package com.rafkot.chatapp.security.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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