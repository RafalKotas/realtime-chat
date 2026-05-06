package com.rafkot.chatapp.message;

import com.rafkot.chatapp.config.UserDetailsImpl;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    MessageService subject;

    @MockitoSpyBean
    MessageRepository messageRepository;

    SimpMessageSendingOperations  messagingTemplate;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        messageRepository = Mockito.mock(MessageRepository.class);
        messagingTemplate = Mockito.mock(SimpMessageSendingOperations.class);
        subject = new MessageService(messageRepository, messagingTemplate, userService);
    }

    @Test
    @WithUserDetails("test-username")
    void shouldThrowExceptionWhenSenderIdIsEqualToRecipientId() {
        // given
        UUID recipientId = UUID.fromString("11111-2222-3333-4444-5555");
        UUID senderId = UUID.fromString("11111-2222-3333-4444-5555");
        SendMessageRequest request = new SendMessageRequest(recipientId, "Hello World");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                senderId,
                "password",
                "username",
                "email@mail.com",
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
        User recipientUser = new User("username", "username@mail.com", "password");
        recipientUser.setId(UUID.fromString("11111-2222-3333-4444-5555"));
        UUID recipientId = recipientUser.getId();

        User senderUser = new User("username2", "username2@mail.com", "password2");
        senderUser.setId(UUID.fromString("22222-3333-4444-5555-6666"));
        UUID senderId = senderUser.getId();

        SendMessageRequest request = new SendMessageRequest(recipientId, "Hello World");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                senderUser.getId(),
                "password",
                "username",
                "email@mail.com",
                Set.of(),
                true,
                true,
                true,
                true
        );
        Principal principal = new UsernamePasswordAuthenticationToken(userDetails, null, Set.of());

        when(userService.getUserById(recipientId)).thenReturn(recipientUser);
        when(userService.getUserById(senderId)).thenReturn(senderUser);
        when(messageRepository.save(any(Message.class))).thenReturn(new Message());

        // when
        subject.sendMessage(request, principal);

        // when + then
        verify(messageRepository).save(any());
    }
}