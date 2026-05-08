package com.rafkot.chatapp.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageHandlingControllerUnitTest {

    @Mock
    private MessageHandlingService messageHandlingService;

    private MessageHandlingController controller;

    @BeforeEach
    void setUp() {
        controller = new MessageHandlingController(messageHandlingService);
    }

    @Test
    void shouldDelegateToMessageService() {
        // given
        SendMessageRequest request = new SendMessageRequest(
                "test-username",
                "hello"
        );

        Principal principal = () -> "testPrincipal";

        // when
        controller.send(request, principal);

        // then
        verify(messageHandlingService).sendMessage(request, principal);
    }

    @Test
    void shouldPropagateExceptionFromMessageService() {
        // given
        SendMessageRequest request = new SendMessageRequest(
                "test-username",
                "hello"
        );

        Principal principal = () -> "testPrincipal";

        doThrow(new IllegalArgumentException("boom"))
                .when(messageHandlingService)
                .sendMessage(request, principal);

        // when + then
        assertThatThrownBy(() -> controller.send(request, principal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("boom");
    }
}