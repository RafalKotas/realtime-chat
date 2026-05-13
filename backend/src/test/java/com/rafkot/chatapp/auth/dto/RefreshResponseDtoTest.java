package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(refreshResponseDto).isNotNull();
        assertThat(refreshResponseDto.accessToken()).isEqualTo("xyz765-new-access-token");
    }
}