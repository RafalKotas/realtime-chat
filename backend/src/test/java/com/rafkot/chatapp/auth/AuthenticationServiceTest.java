package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.LoginResponseDto;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import com.rafkot.chatapp.user.exception.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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
    void shouldAuthenticateUserWithUsernameGivenAndReturnToken() {
        // given
        AuthenticationRequestDto request =
                new AuthenticationRequestDto("testuser", "testpassword");

        String expectedAccessToken = "access-token";
        String expectedRefreshToken = "refresh-token";
        User user = new User("testuser", "testmail@mail", "testpassword");
        user.setId(UUID.randomUUID());

        RefreshToken refreshToken = new RefreshToken(
                1L,
                user,
                expectedRefreshToken,
                Instant.now().plusMillis(86400000)
        );

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(authentication))
                .thenReturn(expectedAccessToken);

        when(refreshTokenService.createRefreshToken(any()))
                .thenReturn(refreshToken);

        // when
        LoginResponseDto result = subject.authenticate(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(authentication);
    }

    @Test
    void shouldAuthenticateUserWithEmailGivenAndReturnToken() {
        // given
        AuthenticationRequestDto request =
                new AuthenticationRequestDto("testuser@mail.com", "testpassword");

        String expectedAccessToken = "access-token";
        String expectedRefreshToken = "refresh-token";

        User testuser = new User("testuser", "testuser@mail.com", "testpassword");
        testuser.setId(UUID.randomUUID());

        RefreshToken refreshToken = new RefreshToken(
                1L,
                testuser,
                expectedRefreshToken,
                Instant.now().plusMillis(86400000)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(authentication))
                .thenReturn(expectedAccessToken);

        when(refreshTokenService.createRefreshToken(any()))
                .thenReturn(refreshToken);

        when(userRepository.findByEmail("testuser@mail.com"))
                .thenReturn(Optional.of(testuser));

        // when
        LoginResponseDto result = subject.authenticate(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(authentication);
    }

    @Test
    void shouldThrowUserValidationExceptionIfAuthenticationFails() {
        // given
        AuthenticationRequestDto request = new AuthenticationRequestDto("testusername", "wrongpass");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testusername");

        when(userRepository.findByUsername("testusername"))
                .thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // when
        UserValidationException ex = assertThrows(
                UserValidationException.class,
                () -> subject.authenticate(request)
        );

        // then
        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(ex.getErrors()).containsEntry("password", "Incorrect password for given login");
    }
}