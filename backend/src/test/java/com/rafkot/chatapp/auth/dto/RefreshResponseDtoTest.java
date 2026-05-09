package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class RefreshResponseDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String accessToken = "xyz765-new-access-token";

        // when
        RefreshResponseDto refreshResponseDto = new RefreshResponseDto(
                accessToken
        );

        // then
        assertNotNull(refreshResponseDto);
        assertEquals("xyz765-new-access-token", refreshResponseDto.accessToken());
    }
}