package com.rafkot.chatapp.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
class UserProfileDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String username = "exampleUsername";
        String email = "exampleUserEmail@mail.com";
        String joinedAtAndCreatedAt = Instant.now().toString();

        // when
        UserProfileDto userProfileDto = new UserProfileDto(
                username,
                email,
                joinedAtAndCreatedAt,
                joinedAtAndCreatedAt
        );

        // then
        assertNotNull(userProfileDto);
        assertThat(userProfileDto.username()).isEqualTo("exampleUsername");
        assertThat(userProfileDto.email()).isEqualTo("exampleUserEmail@mail.com");
        assertThat(userProfileDto.joinedAt()).isEqualTo(joinedAtAndCreatedAt);
        assertThat(userProfileDto.lastEditedAt()).isEqualTo(joinedAtAndCreatedAt);
    }
}