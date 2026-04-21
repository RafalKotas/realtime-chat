package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.exception.UserValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User user) {
        final Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByEmail(user.getEmail())) {
            errors.put("email", "Email [%s] is already taken".formatted(user.getEmail()));
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            errors.put("username", "Username [%s] is already taken".formatted(user.getUsername()));
        }

        if (!errors.isEmpty()) {
            throw new UserValidationException(CONFLICT, errors);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}
