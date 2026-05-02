package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefreshRequestDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String refreshToken = "xyz-test-refresh-token";

        // when
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto(
                refreshToken
        );

        // then
        assertNotNull(refreshRequestDto);
        assertEquals("xyz-test-refresh-token", refreshRequestDto.refreshToken());
    }
}