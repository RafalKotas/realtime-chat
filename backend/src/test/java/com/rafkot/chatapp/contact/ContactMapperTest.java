package com.rafkot.chatapp.contact;

import com.rafkot.chatapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ContactMapperTest {

    ContactMapper subject;

    @BeforeEach
    void setUp() {
        subject = new ContactMapper();
    }

    @Test
    void shouldMapContactToContactDto() {
        // given
        User contactUser = new User();
        contactUser.setUsername("test-username");

        Contact testContact = new Contact();
        testContact.setId(UUID.fromString("11111-22222-33333-44444-55555"));
        testContact.setMember(contactUser);

        // when
        ContactDto result = subject.toContactDto(testContact);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(UUID.fromString("11111-22222-33333-4444-55555"));
        assertThat(result.username()).isEqualTo("test-username");
        assertThat(result.avatarUrl()).isNull();
    }
}