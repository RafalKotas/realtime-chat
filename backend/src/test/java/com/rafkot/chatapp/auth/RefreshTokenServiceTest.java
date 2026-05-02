package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import com.rafkot.chatapp.user.exception.UserNotFoundException;
import com.rafkot.chatapp.user.exception.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @InjectMocks
    RefreshTokenService subject;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    UserRepository userRepository;

    Long milliSecondsInHour = 3600000L;

    @BeforeEach
    void setUp() throws Exception {
        subject = new RefreshTokenService(refreshTokenRepository, userRepository);
        Field field = RefreshTokenService.class.getDeclaredField("refreshTokenDurationMs");
        field.setAccessible(true);
        field.set(subject, milliSecondsInHour); // 1h
    }

    @Test
    void shouldCreateRefreshToken() {
        // given
        UUID userId = UUID.randomUUID();
        User testUser = new User("testuser", "testemail@mail.com", "testpswd");
        testUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(refreshTokenRepository.save(org.mockito.Mockito.any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        RefreshToken refreshToken = subject.createRefreshToken(userId);

        // then
        assertNotNull(refreshToken);
        assertNotNull(refreshToken.getToken());
        assertNotNull(refreshToken.getExpiryDate());
        assertThat(refreshToken.getUser()).isEqualTo(testUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> subject.createRefreshToken(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(userId.toString());
    }

    @Test
    void shouldValidateRefreshToken() {
        // given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().minusMillis(milliSecondsInHour));

        // then
        boolean result = subject.isTokenExpired(refreshToken);

        //
        assertThat(result).isTrue();
    }

    @Test
    void shouldRejectInvalidRefreshToken() {
        // given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(milliSecondsInHour));

        // then
        boolean result = subject.isTokenExpired(refreshToken);

        //
        assertThat(result).isFalse();
    }

    @Test
    void shouldValidateTokenWithSuccessAndReturnCorrespondingUser() {
        // given
        String refreshTokenValue = "test-refresh-token-value";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryDate(Instant.now().plusMillis(milliSecondsInHour));

        User testUser = new User();
        refreshToken.setUser(testUser);

        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));

        // when
        User result = subject.validateRefreshToken(refreshTokenValue);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowInvalidRefreshTokenExceptionWhenRefreshTokenNotFound() {
        // given
        String refreshTokenValue = "test-refresh-token-value";

        when(refreshTokenRepository.findByToken(refreshTokenValue)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> subject.validateRefreshToken("test-refresh-token-value"))
                .isInstanceOf(UserValidationException.class);
    }

    @Test
    void shouldThrowRefreshTokenExpiredExceptionWhenRefreshTokenExpiryDateIsBeforeNow() {
        // given
        String refreshTokenValue = "test-refresh-token-value";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().minusMillis(milliSecondsInHour));

        when(refreshTokenRepository.findByToken(refreshTokenValue)).thenReturn(Optional.of(refreshToken));

        // when + then
        assertThatThrownBy(() -> subject.validateRefreshToken("test-refresh-token-value"))
                .isInstanceOf(UserValidationException.class);
    }
}