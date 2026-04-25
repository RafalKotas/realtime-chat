package com.rafkot.chatapp.user.mapper;

import com.rafkot.chatapp.auth.dto.RegistrationRequestDto;
import com.rafkot.chatapp.auth.dto.RegistrationResponseDto;
import com.rafkot.chatapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRegistrationMapperTest {

    UserRegistrationMapper subject;

    @BeforeEach
    void setUp() {
        subject = new UserRegistrationMapper();
    }

    @Test
    void shouldMapRegistrationRequestDtoDTOtoUserEntity() {
        // given
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
                "testuser",
                "testuser@mail.com",
                "testpswd"
        );

        // when
        User result = subject.toEntity(registrationRequestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPassword()).isEqualTo("testpswd");
        assertThat(result.getEmail()).isEqualTo("testuser@mail.com");
        assertThat(result.getCreatedDate()).isNull();
        assertThat(result.getModifiedDate()).isNull();
    }

    @Test
    void shouldMapEntityToRegistrationResponseDto() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpswd");
        user.setEmail("testuser@mail.com");

        // when
        RegistrationResponseDto result = subject.toRegistrationResponseDto(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("testuser");
        assertThat(result.email()).isEqualTo("testuser@mail.com");
    }
}