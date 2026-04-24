package com.rafkot.chatapp.user;

import com.rafkot.chatapp.UserMapper;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserService subject;

    @Mock
    private UserRepository userRepository;

    UserMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new UserService(userRepository);
        mapper = new UserMapper();
    }

    @Test
    void shouldReturnUserWhenUserExists() {
        // given
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        // when
        User result = subject.getUserByUsername("testuser");

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
    void shouldMapUserToUserProfileDto() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@mail.com");

        // when
        UserProfileDto result = mapper.toUserProfileDto(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("testuser");
        assertThat(result.email()).isEqualTo("testuser@mail.com");
    }
}