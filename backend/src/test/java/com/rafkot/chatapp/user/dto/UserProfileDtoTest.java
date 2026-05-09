package com.rafkot.chatapp.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class UserProfileDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        UUID id = UUID.randomUUID();
        String username = "exampleUsername";
        String email = "exampleUserEmail@mail.com";

        // when
        UserProfileDto userProfileDto = new UserProfileDto(
                id,
                username,
                email
        );

        // then
        assertNotNull(userProfileDto);
        assertEquals(id, userProfileDto.id());
        assertEquals("exampleUsername", userProfileDto.username());
        assertEquals("exampleUserEmail@mail.com", userProfileDto.email());
    }
}