package com.rafkot.chatapp.user.mapper;

import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper subject;

    @BeforeEach
    void setUp() {
        subject = new UserMapper();
    }

    @Test
    void shouldMapUserToUserProfileDto() {
        // given
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("secret")
                .build();

        UUID userId = user.getId();
        user.setId(userId);

        // when
        UserProfileDto result = subject.toUserProfileDto(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.username()).isEqualTo("testuser");
        assertThat(result.email()).isEqualTo("test@example.com");
    }
}