package com.rafkot.chatapp.message;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SendMessageRequestTest {

    private SendMessageRequest subject;

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        UUID recipientId = UUID.randomUUID();
        String content = "Hello World!";

        // when
        subject = new SendMessageRequest(
                recipientId, content
        );

        // then
        assertThat(subject).isNotNull();
        assertThat(subject.recipientId()).isEqualTo(recipientId);
        assertThat(subject.content()).isEqualTo("Hello World!");
    }
}