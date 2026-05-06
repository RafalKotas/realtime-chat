package com.rafkot.chatapp.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    private MessageController controller;

    @BeforeEach
    void setUp() {
        controller = new MessageController(messageService);
    }

    @Test
    void shouldDelegateToMessageService() {
        // given
        SendMessageRequest request = new SendMessageRequest(
                UUID.randomUUID(),
                "hello"
        );

        Principal principal = () -> "testPrincipal";

        // when
        controller.send(request, principal);

        // then
        verify(messageService).sendMessage(request, principal);
    }

    @Test
    void shouldPropagateExceptionFromMessageService() {
        // given
        SendMessageRequest request = new SendMessageRequest(
                UUID.randomUUID(),
                "hello"
        );

        Principal principal = () -> "testPrincipal";

        doThrow(new IllegalArgumentException("boom"))
                .when(messageService)
                .sendMessage(request, principal);

        // when + then
        assertThatThrownBy(() -> controller.send(request, principal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("boom");
    }
}