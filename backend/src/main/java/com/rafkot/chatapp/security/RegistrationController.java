package com.rafkot.chatapp.security;

import com.rafkot.chatapp.security.dto.RegistrationRequestDto;
import com.rafkot.chatapp.security.dto.RegistrationResponseDto;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRegistrationMapper;
import com.rafkot.chatapp.user.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;

    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(
            @Valid @RequestBody final RegistrationRequestDto registrationDTO) {

        final User registeredUser = userRegistrationService
                .registerUser(userRegistrationMapper.toEntity(registrationDTO));


        log.info("User [%s with email %s] registered successfully".formatted(registeredUser.getUsername(), registeredUser.getEmail()));
        return ResponseEntity.ok(
                userRegistrationMapper.toRegistrationResponseDto(registeredUser)
        );
    }
}
