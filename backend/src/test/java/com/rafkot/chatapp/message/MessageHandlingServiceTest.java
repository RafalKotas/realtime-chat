package com.rafkot.chatapp.message;

import com.rafkot.chatapp.config.UserDetailsImpl;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.security.Principal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MessageHandlingServiceTest {

    MessageHandlingService subject;

    @MockitoSpyBean
    MessageRepository messageRepository;

    SimpMessageSendingOperations  messagingTemplate;

    @Mock
    UserService userService;

    @Mock
    MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageRepository = Mockito.mock(MessageRepository.class);
        messagingTemplate = Mockito.mock(SimpMessageSendingOperations.class);
        subject = new MessageHandlingService(messageRepository, messagingTemplate, userService, messageMapper);
    }

    @Test
    @WithUserDetails("test-username")
    void shouldThrowExceptionWhenSenderUsernameIsEqualToRecipientUsername() {
        // given
        UUID senderId = UUID.fromString("11111-2222-3333-4444-5555");
        String senderUsername = "test-user";
        String senderEmail = "test-user@mail.com";
        String senderPassword = "test-user";
        User senderUser = new User(senderUsername, senderEmail, senderPassword);
        senderUser.setId(senderId);

        SendMessageRequest request = new SendMessageRequest("test-user", "Hello World");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                senderId,
                "test-user",
                "test-user",
                "test-user@mail.com",
                Set.of(),
                true,
                true,
                true,
                true
        );
        Principal principal = new UsernamePasswordAuthenticationToken(userDetails, null, Set.of());

        // when + then
        assertThatThrownBy(() -> subject.sendMessage(request, principal))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldSendMessagesWhenUserAuthorizedAndRecipientIdOtherThanSenderId() {
        // given
        String senderUsername = "username2";
        String senderEmail = "username2@mail.com";
        String senderPassword = "password2";
        User senderUser = new User(senderUsername, senderEmail, senderPassword);
        senderUser.setId(UUID.fromString("22222-3333-4444-5555-6666"));

        User recipientUser = new User("username", "username@mail.com", "password");
        recipientUser.setId(UUID.fromString("11111-2222-3333-4444-5555"));
        String recipientUsername = recipientUser.getUsername();

        SendMessageRequest request = new SendMessageRequest(recipientUsername, "Hello World");

        Instant now =  Instant.now();

        Message testMessage = generateTestMessage(senderUser, recipientUser, now);

        MessageResponseDto response = new MessageResponseDto(
                "Hello World",
                now,
                now,
                senderUsername,
                recipientUsername,
                UUID.randomUUID());

        UserDetailsImpl userDetails = new UserDetailsImpl(
                senderUser.getId(),
                "password2",
                senderUsername,
                "username2@mail.com",
                Set.of(),
                true,
                true,
                true,
                true
        );
        Principal principal = new UsernamePasswordAuthenticationToken(userDetails, null, Set.of());

        when(userService.getUserByUsername(recipientUsername)).thenReturn(recipientUser);
        when(userService.getUserByUsername(senderUsername)).thenReturn(senderUser);
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);
        when(messageMapper.mapMessageToDto(testMessage)).thenReturn(response);

        // when
        subject.sendMessage(request, principal);

        // when + then
        verify(messageRepository).save(any());
    }

    private Message generateTestMessage(User senderUser, User recipientUser, Instant now) {
        Message testMessage = new Message();
        testMessage.setSender(senderUser);
        testMessage.setRecipient(recipientUser);
        testMessage.setContent("Hello World");
        testMessage.setId(UUID.randomUUID());

        testMessage.setModifiedDate(now);
        testMessage.setCreatedDate(now);

        return testMessage;
    }
}