package com.rafkot.chatapp.user.mapper;

import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        Instant now = Instant.now();
        LocalDateTime dateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        String dayOfMonth = dateTime.getDayOfMonth() + "";
        String month = dateTime.getMonth().toString().charAt(0) + dateTime.getMonth().toString().substring(1, 3).toLowerCase();
        String year = dateTime.getYear() + "";
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("secret")
                .build();
        user.setModifiedDate(now);
        user.setCreatedDate(now);

        UUID userId = user.getId();
        user.setId(userId);

        // when
        UserProfileDto result = subject.toUserProfileDto(user);

        // then
        String expectedDateString = dayOfMonth + " " + month + " " + year;
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("testuser");
        assertThat(result.email()).isEqualTo("test@example.com");
        assertThat(result.joinedAt()).isEqualTo(expectedDateString);
        assertThat(result.lastEditedAt()).isEqualTo(expectedDateString);
    }
}