package com.rafkot.chatapp.user.mapper;

import com.rafkot.chatapp.auth.dto.RegistrationRequestDto;
import com.rafkot.chatapp.auth.dto.RegistrationResponseDto;
import com.rafkot.chatapp.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

    public User toEntity(RegistrationRequestDto request) {
        final User user = new User();

        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(request.password());

        return user;
    }

    public RegistrationResponseDto toRegistrationResponseDto(
            final User user) {

        return new RegistrationResponseDto(
                user.getUsername(), user.getEmail()
        );
    }

}
