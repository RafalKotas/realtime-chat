package com.rafkot.chatapp.message;

import com.rafkot.chatapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class MessageMapperTest {

    MessageMapper subject;

    @BeforeEach
    void setUp() {
        subject = new MessageMapper();
    }

    @Test
    void shouldMapMessageToMessageResponseDto() {
        // given
        User testSender = new User("testuser1", "testuser1@mail.com", "testpass1");
        User testRecipient = new User("testuser2", "testuser2@mail.com", "testpass2");

        String content = "Hello World!";
        Message testMessage = new Message(testSender, testRecipient, content);

        Instant now  = Instant.now();
        testMessage.setCreatedDate(now);
        testMessage.setModifiedDate(now);

        UUID uuid = UUID.fromString("11111111-2222-3333-4444-555555555555");
        testMessage.setId(uuid);

        // when
        MessageResponseDto result = subject.mapMessageToDto(testMessage);

        // then
        assertThat(result).isNotNull();

        assertThat(result.content()).isNotNull().isEqualTo("Hello World!");
        assertThat(result.createdDate()).isNotNull().isEqualTo(now);
        assertThat(result.modifiedDate()).isNotNull().isEqualTo(now);
        assertThat(result.senderUsername()).isNotNull().isEqualTo("testuser1");
        assertThat(result.receiverUsername()).isNotNull().isEqualTo("testuser2");
        assertThat(result.messageId()).isNotNull().isEqualTo(UUID.fromString("11111111-2222-3333-4444-555555555555"));
    }
}