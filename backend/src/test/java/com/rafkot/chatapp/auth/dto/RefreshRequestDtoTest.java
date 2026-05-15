package com.rafkot.chatapp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
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
        assertThat(refreshRequestDto).isNotNull();
        assertThat(refreshRequestDto.refreshToken()).isEqualTo("xyz-test-refresh-token");
    }
}