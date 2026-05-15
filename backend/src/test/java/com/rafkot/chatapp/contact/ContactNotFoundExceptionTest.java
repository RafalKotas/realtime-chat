package com.rafkot.chatapp.contact;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ContactNotFoundExceptionTest {

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        ContactNotFoundException contactNotFoundException = new ContactNotFoundException(uuid);

        // then
        assertThat(contactNotFoundException).isNotNull()
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessage("Contact not found: " + uuid);
    }
}