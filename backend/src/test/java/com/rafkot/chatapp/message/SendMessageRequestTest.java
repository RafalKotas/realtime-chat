package com.rafkot.chatapp.message;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class SendMessageRequestTest {

    private SendMessageRequest subject;

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        String recipientName = "test-recipient";
        String content = "Hello World!";

        // when
        subject = new SendMessageRequest(
                recipientName, content
        );

        // then
        assertThat(subject).isNotNull();
        assertThat(subject.recipientUsername()).isEqualTo("test-recipient");
        assertThat(subject.content()).isEqualTo("Hello World!");
    }
}