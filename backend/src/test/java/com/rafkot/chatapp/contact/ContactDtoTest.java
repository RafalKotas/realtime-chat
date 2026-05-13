package com.rafkot.chatapp.contact;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
class ContactDtoTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        UUID uuid = UUID.randomUUID();
        String username = "testUser";
        String avatarUrl = "www.testAvatar.com";

        // when
        ContactDto contactDto = new ContactDto(
                uuid,
                username,
                avatarUrl
        );

        // then
        assertNotNull(contactDto);
        assertThat(contactDto.id()).isEqualTo(uuid);
        assertThat(contactDto.username()).isEqualTo("testUser");
        assertThat(contactDto.avatarUrl()).isEqualTo("www.testAvatar.com");
    }
}