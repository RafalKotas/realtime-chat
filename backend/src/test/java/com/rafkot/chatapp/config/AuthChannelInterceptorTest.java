package com.rafkot.chatapp.config;

import com.rafkot.chatapp.auth.JwtService;
import com.rafkot.chatapp.user.JpaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthChannelInterceptorTest {

    @Mock
    JwtService jwtService;

    @Mock
    JpaUserDetailsService jpaUserDetailsService;

    AuthChannelInterceptor subject;

    @BeforeEach
    void setUp() {
        subject = new AuthChannelInterceptor(jwtService, jpaUserDetailsService);
    }

    private Message<byte[]> buildConnectMessage(String token) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.addNativeHeader("Authorization", "Bearer " + token);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }

    @Test
    void shouldAuthenticateUserOnConnect() {
        // given
        String token = "valid-token";
        String username = "test-username";
        UUID userID = UUID.fromString("11111-22222-33333-44444-55555");

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(userID)
                .username(username)
                .email("test-username@mail.com")
                .password("password")
                .authorities(Set.of())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        when(jwtService.getUsernameFromToken(token)).thenReturn(username);
        when(jpaUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        Message<?> message = buildConnectMessage(token);

        // when
        Message<?> result = subject.preSend(message, mock(MessageChannel.class));

        // then
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(result, StompHeaderAccessor.class);
        assertThat(accessor.getUser()).isInstanceOf(Authentication.class);

        Authentication auth = (Authentication) accessor.getUser();
        assertThat(auth.getPrincipal()).isEqualTo(userDetails);

        verify(jwtService).getUsernameFromToken(token);
        verify(jpaUserDetailsService).loadUserByUsername(username);
    }

    @Test
    void shouldThrowWhenAuthorizationHeaderMissing() {
        // given
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        Message<?> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        // when + then
        assertThatThrownBy(() -> subject.preSend(message, mock(MessageChannel.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing Authorization header");
    }

    @Test
    void shouldThrowWhenTokenInvalid() {
        // given
        String token = "invalid-token";
        Message<?> message = buildConnectMessage(token);

        when(jwtService.getUsernameFromToken(token)).thenThrow(new IllegalArgumentException("Invalid token"));

        // when + then
        assertThatThrownBy(() -> subject.preSend(message, mock(MessageChannel.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid token");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // given
        String token = "valid-token";

        Message<?> message = buildConnectMessage(token);

        when(jwtService.getUsernameFromToken(token)).thenReturn(null);

        // when + then
        assertThatThrownBy(() -> subject.preSend(message, mock(MessageChannel.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid token");
    }

    @Test
    void shouldIgnoreNonConnectFrames() {
        // given
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SEND);
        accessor.setLeaveMutable(true);
        Message<?> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        // when
        Message<?> result = subject.preSend(message, mock(MessageChannel.class));

        // then
        assertThat(result).isSameAs(message);
        verifyNoInteractions(jwtService, jpaUserDetailsService);
    }

    @Test
    void shouldIgnoreWhenAccessorIsNull() {
        // given
        Message<?> message = MessageBuilder.withPayload(new byte[0]).build();

        // when
        Message<?> result = subject.preSend(message, mock(MessageChannel.class));

        // then
        assertThat(result).isSameAs(message);
        verifyNoInteractions(jwtService, jpaUserDetailsService);
    }
}