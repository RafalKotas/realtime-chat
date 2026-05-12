package com.rafkot.chatapp.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class UserServiceTest {

    private UserService subject;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void shouldReturnUserByUsernameWhenUserExists() {
        // given
        User user = new User();
        String username = "testuser";
        user.setUsername(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        // when
        User result = subject.getUserByUsername(username);

        // then
        assertThat(result).isEqualTo(user);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> subject.getUserByUsername("missing"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("The user account has been deleted or inactivated");
    }

    @Test
    void shouldGetUserByIdWhenUserExists() {
        // given
        User user = new User();
        UUID userId = UUID.fromString("11111-22222-33333-44444-55555");
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // when
        User result = subject.getUserById(userId);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsNotMatch() {
        // given
        String password = "abcdef";
        String confirmPassword = "abxdef";

        // when & then
        assertThatThrownBy(() -> subject.changePassword("testUser", password, confirmPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password do not match");
    }

    @Test
    void shouldChangePasswordWhenPasswordMatch() {
        // given
        String password = "abcdef";
        String confirmPassword = "abcdef";
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));

        // when
        subject.changePassword("testUser", password, confirmPassword);

        // then
        verify(userRepository).findByUsername("testUser");
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any());
    }
}