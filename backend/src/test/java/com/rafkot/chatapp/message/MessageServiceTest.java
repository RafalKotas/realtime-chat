package com.rafkot.chatapp.message;

import com.rafkot.chatapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    MessageService subject;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        subject = new MessageService(messageRepository, messageMapper);
    }

    @Test
    void getGroupedUserChatsMessages() {
        // given - data
        User testUser1 = new User("testUser1", "testUser1@mail.com", "testPass1");
        testUser1.setId(UUID.fromString("11111-22222-33333-44444-55555"));

        User testUser2 = new User("testUser2", "testUser2@mail.com", "testPass2");
        testUser2.setId(UUID.fromString("22222-33333-44444-55555-66666"));

        User testUser3 = new User("testUser3", "testUser3@mail.com", "testPass3");
        testUser3.setId(UUID.fromString("33333-44444-55555-66666-77777"));

        User testUser4 = new User("testUser4", "testUser4@mail.com", "testPass4");
        testUser4.setId(UUID.fromString("44444-55555-66666-77777-88888"));

        List<String> testUser1ConversationPartners = new ArrayList<>(List.of(testUser2.getUsername(), testUser3.getUsername(), testUser4.getUsername()));

        // testUser1 <-> testUser2
        Message firstTestMessage = new Message(testUser1, testUser4, "testMessage12-1");
        Instant firstTestMessageDate = Instant.now().minusSeconds(60);
        firstTestMessage.setCreatedDate(firstTestMessageDate);
        firstTestMessage.setModifiedDate(firstTestMessageDate);
        MessageResponseDto firstMessageResponseDto = new MessageResponseDto(
                "testMessage12-1",
                firstTestMessageDate,
                firstTestMessageDate,
                "testUser1",
                "testUser2",
                UUID.fromString("11111-11111-11111-11111-11111")
        );

        Message secondTestMessage = new Message(testUser1, testUser4, "testMessage12-2");
        Instant secondTestMessageDate = firstTestMessageDate.plusSeconds(10);
        secondTestMessage.setCreatedDate(secondTestMessageDate);
        secondTestMessage.setModifiedDate(secondTestMessageDate);
        MessageResponseDto secondMessageResponseDto = new MessageResponseDto(
                "testMessage12-2",
                secondTestMessageDate,
                secondTestMessageDate,
                "testUser1",
                "testUser2",
                UUID.fromString("22222-22222-22222-22222-22222")
        );

        List<Message> testUser1testUser2Messages = List.of(firstTestMessage, secondTestMessage);

        // testUser1 <-> testUser3
        Message thirdTestMessage = new Message(testUser1, testUser4, "testMessage13-1");
        Instant thirdTestMessageDate = firstTestMessageDate.plusSeconds(20);
        thirdTestMessage.setCreatedDate(thirdTestMessageDate);
        thirdTestMessage.setModifiedDate(thirdTestMessageDate);
        MessageResponseDto thirdMessageResponseDto = new MessageResponseDto(
                "testMessage13-1",
                thirdTestMessageDate,
                thirdTestMessageDate,
                "testUser1",
                "testUser3",
                UUID.fromString("33333-33333-33333-33333-33333")
        );

        Message fourthTestMessage = new Message(testUser1, testUser3, "testMessage13-2");
        Instant fourthTestMessageDate = firstTestMessageDate.plusSeconds(30);
        fourthTestMessage.setCreatedDate(fourthTestMessageDate);
        fourthTestMessage.setModifiedDate(fourthTestMessageDate);
        MessageResponseDto fourthMessageResponseDto = new MessageResponseDto(
                "testMessage13-2",
                fourthTestMessageDate,
                fourthTestMessageDate,
                "testUser1",
                "testUser3",
                UUID.fromString("44444-44444-44444-44444-44444")
        );

        List<Message> testUser1testUser3Messages = List.of(thirdTestMessage, fourthTestMessage);

        // testUser1 <-> testUser4
        Message fifthTestMessage = new Message(testUser1, testUser4, "testMessage14-1");
        Instant fifthTestMessageDate = firstTestMessageDate.plusSeconds(40);
        fifthTestMessage.setCreatedDate(fifthTestMessageDate);
        fifthTestMessage.setModifiedDate(fifthTestMessageDate);
        MessageResponseDto fifthMessageResponseDto = new MessageResponseDto(
                "testMessage14-1",
                fifthTestMessageDate,
                fifthTestMessageDate,
                "testUser1",
                "testUser2",
                UUID.fromString("55555-55555-55555-55555-55555")
        );

        Message sixthTestMessage = new Message(testUser1, testUser4, "testMessage14-2");
        Instant sixTestMessageDate = firstTestMessageDate.plusSeconds(50);
        sixthTestMessage.setCreatedDate(sixTestMessageDate);
        sixthTestMessage.setModifiedDate(sixTestMessageDate);
        MessageResponseDto sixthMessageResponseDto = new MessageResponseDto(
                "testMessage14-2",
                sixTestMessageDate,
                sixTestMessageDate,
                "testUser1",
                "testUser2",
                UUID.fromString("66666-66666-66666-66666-66666")
        );

        List<Message> testUser1testUser4Messages = List.of(fifthTestMessage, sixthTestMessage);


        // given - mocks
        when(messageRepository.findConversationPartners(testUser1.getId())).thenReturn(testUser1ConversationPartners);
        when(messageRepository.findConversationMessages(testUser1.getId(), testUser2.getUsername())).thenReturn(testUser1testUser2Messages);
        when(messageRepository.findConversationMessages(testUser1.getId(), testUser3.getUsername())).thenReturn(testUser1testUser3Messages);
        when(messageRepository.findConversationMessages(testUser1.getId(), testUser4.getUsername())).thenReturn(testUser1testUser4Messages);

        when(messageMapper.mapMessageToDto(firstTestMessage)).thenReturn(firstMessageResponseDto);
        when(messageMapper.mapMessageToDto(secondTestMessage)).thenReturn(secondMessageResponseDto);
        when(messageMapper.mapMessageToDto(thirdTestMessage)).thenReturn(thirdMessageResponseDto);
        when(messageMapper.mapMessageToDto(fourthTestMessage)).thenReturn(fourthMessageResponseDto);
        when(messageMapper.mapMessageToDto(fifthTestMessage)).thenReturn(fifthMessageResponseDto);
        when(messageMapper.mapMessageToDto(sixthTestMessage)).thenReturn(sixthMessageResponseDto);

        // when
        Map<String, List<MessageResponseDto>> conversationPartnersToChatMessagesMap = subject.getGroupedUserChatsMessages(testUser1.getId().toString());

        // then
        assertThat(conversationPartnersToChatMessagesMap)
                .isNotNull()
                .hasSize(3);
    }
}