package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.LoginResponseDto;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Optional;

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
    private RefreshTokenService refreshTokenService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        subject = new AuthenticationService(authenticationManager, jwtService, userRepository, refreshTokenService);
    }

    @Test
    void shouldAuthenticateUserAndReturnToken() {
        // given
        AuthenticationRequestDto request =
                new AuthenticationRequestDto("testuser", "testpassword");

        String expectedAccessToken = "access-token";
        String expectedRefreshToken = "refresh-token";

        RefreshToken refreshToken = new RefreshToken(
                1L,
                new User("testuser", "testmail@mail", "testpassword"),
                expectedRefreshToken,
                Instant.now().plusMillis(86400000)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(authentication))
                .thenReturn(expectedAccessToken);

        when(refreshTokenService.createRefreshToken(any()))
                .thenReturn(refreshToken);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(new User("testuser", "testmail@mail", "testpassword")));

        // when
        LoginResponseDto result = subject.authenticate(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(authentication);
    }
}