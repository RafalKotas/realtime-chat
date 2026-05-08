package com.rafkot.chatapp.config;

import com.rafkot.chatapp.auth.JwtService;
import com.rafkot.chatapp.user.JpaUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final JpaUserDetailsService jpaUserDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            try {
                List<String> authHeaders = accessor.getNativeHeader("Authorization");
                log.info("WS CONNECT headers: {}", authHeaders);

                // TODO - implement custom exception
                if (authHeaders == null) {
                    throw new IllegalArgumentException("Missing Authorization header");
                }

                String token = authHeaders.getFirst().replace("Bearer ", "");
                log.info("WS CONNECT token: {}", token);

                String username = jwtService.getUsernameFromToken(token);
                log.info("WS CONNECT username from token: {}", username);

                // TODO - implement custom exception
                if (username == null) {
                    throw new IllegalArgumentException("Invalid token");
                }

                UserDetailsImpl userDetails =
                        (UserDetailsImpl) jpaUserDetailsService.loadUserByUsername(username);
                log.info("WS CONNECT userDetails id={}, username={}",
                        userDetails.getId(), userDetails.getUsername());

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                accessor.setUser(authentication);
                log.info("WS CONNECT setUser done");
            } catch (Exception e) {
                log.error("WS CONNECT error", e);
                throw e;
            }
        }

        return message;
    }
}
