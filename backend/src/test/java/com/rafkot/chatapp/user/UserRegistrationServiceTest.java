package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.exception.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class UserRegistrationServiceTest {

    private UserRegistrationService subject;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new UserRegistrationService(userRepository, passwordEncoder);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        User user = new User();
        user.setEmail("test@mail.com");
        user.setUsername("testuser");
        user.setPassword("raw");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("raw")).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        User result = subject.registerUser(user);

        // then
        assertThat(result.getPassword()).isEqualTo("encoded");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenEmailExists() {
        // given
        User user = new User();
        user.setEmail("test@mail.com");
        user.setUsername("testuser");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        // when + then
        assertThatThrownBy(() -> subject.registerUser(user))
                .isInstanceOf(UserValidationException.class)
                .satisfies(ex -> {
                    UserValidationException e = (UserValidationException) ex;
                    assertThat(e.getErrors())
                            .containsKey("email");
                });
    }

    @Test
    void shouldThrowWhenUsernameExists() {
        // given
        User user = new User();
        user.setEmail("test@mail.com");
        user.setUsername("testuser");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // when + then
        assertThatThrownBy(() -> subject.registerUser(user))
                .isInstanceOf(UserValidationException.class)
                .satisfies(ex -> {
                    UserValidationException e = (UserValidationException) ex;
                    assertThat(e.getErrors())
                            .containsKey("username");
                });
    }

    @Test
    void shouldThrowWhenBothEmailAndUsernameExist() {
        // given
        User user = new User();
        user.setEmail("test@mail.com");
        user.setUsername("testuser");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // when + then
        assertThatThrownBy(() -> subject.registerUser(user))
                .isInstanceOf(UserValidationException.class)
                .satisfies(ex -> {
                    UserValidationException e = (UserValidationException) ex;

                    assertThat(e.getErrors())
                            .containsKeys("email", "username");
                });
    }
}