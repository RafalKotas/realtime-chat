package com.rafkot.chatapp.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class JpaUserDetailsServiceTest {

    private JpaUserDetailsService subject;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new JpaUserDetailsService(userRepository);
    }

    @Test
    void shouldReturnUserDetailsWhenUserExists() {
        // given
        String username = "testuser";

        com.rafkot.chatapp.user.User user = new com.rafkot.chatapp.user.User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        // when
        UserDetails result = subject.loadUserByUsername(username);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository).findByUsername(username);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // given
        String username = "unknown";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> subject.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with username [%s] not found".formatted(username));
    }
}