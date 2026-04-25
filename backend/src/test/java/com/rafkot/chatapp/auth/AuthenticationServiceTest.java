package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.AuthenticationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    AuthenticationService subject;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        subject = new AuthenticationService(authenticationManager, jwtService);
    }

    @Test
    void shouldAuthenticateUserAndReturnToken() {
        // given
        AuthenticationRequestDto request =
                new AuthenticationRequestDto("testuser", "testpassword");

        String expectedToken = "jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(authentication))
                .thenReturn(expectedToken);

        // when
        AuthenticationResponseDto result = subject.authenticate(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo(expectedToken);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(authentication);
    }
}