package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.AuthenticationResponseDto;
import com.rafkot.chatapp.user.UserRepository;
import com.rafkot.chatapp.user.exception.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDto authenticate(
            final AuthenticationRequestDto request
    ) {
        final Map<String, String> errors = new HashMap<>();

        boolean isEmail = isEmail(request.login());

        String username = isEmail ? userRepository.findUsernameByEmail(request.login()) : request.login();

        if  (username == null) {
            errors.put("login", "User with this username or email doesn't exist");
        }

        try {
            final UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken
                    .unauthenticated(username, request.password());

            final Authentication authentication = authenticationManager
                    .authenticate(authToken);

            final String token = jwtService.generateToken(authentication);
            return new AuthenticationResponseDto(token);
        } catch (AuthenticationException e) {
            if (errors.isEmpty()) {
                errors.put("password", "Incorrect password for given login");
            }
            throw new UserValidationException(HttpStatus.UNAUTHORIZED, errors);
        }
    }

    private boolean isEmail(String login) {
        return login.contains("@");
    }
}
