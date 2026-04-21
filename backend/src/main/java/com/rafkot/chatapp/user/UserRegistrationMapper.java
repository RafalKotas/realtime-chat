package com.rafkot.chatapp.user;

import com.rafkot.chatapp.security.dto.RegistrationRequestDto;
import com.rafkot.chatapp.security.dto.RegistrationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

    public static User toEntity(RegistrationRequestDto registrationRequestDtoDTO) {
        final User user = new User();

        user.setEmail(registrationRequestDtoDTO.email());
        user.setUsername(registrationRequestDtoDTO.username());
        user.setPassword(registrationRequestDtoDTO.password());

        return user;
    }

    public RegistrationResponseDto toRegistrationResponseDto(
            final User user) {

        return new RegistrationResponseDto(
                user.getEmail(), user.getUsername()
        );
    }

}
