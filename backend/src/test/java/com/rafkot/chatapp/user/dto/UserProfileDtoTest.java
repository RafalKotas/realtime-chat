package com.rafkot.chatapp.user.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String username = "exampleUsername";
        String email = "exampleUserEmail@mail.com";

        // when
        UserProfileDto userProfileDto = new UserProfileDto(
                username,
                email
        );

        // then
        assertNotNull(userProfileDto);
        assertEquals("exampleUsername", userProfileDto.username());
        assertEquals("exampleUserEmail@mail.com", userProfileDto.email());
    }
}