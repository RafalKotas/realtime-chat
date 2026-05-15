package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.LoginResponseDto;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import com.rafkot.chatapp.user.exception.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public LoginResponseDto authenticate(
            final AuthenticationRequestDto request
    ) {
        String login = request.login();

        boolean isEmail = isEmail(login);

        User user = (isEmail
                ? userRepository.findByEmail(login)
                : userRepository.findByUsername(login))
                .orElseThrow(() -> new UserValidationException(
                        HttpStatus.UNAUTHORIZED,
                        Map.of("login", "User with this username or email doesn't exist")
                ));

        try {
            final UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken
                    .unauthenticated(
                            user.getUsername(), // always authenticate by username for Spring Security
                            request.password()
                    );

            final Authentication authentication = authenticationManager.authenticate(authToken);

            final String accessToken = jwtService.generateToken(authentication);
            final RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

            return new LoginResponseDto(user.getUsername(), user.getId().toString(), accessToken, refreshToken.getToken());
        } catch (AuthenticationException e) {
            throw new UserValidationException(
                    HttpStatus.UNAUTHORIZED,
                    Map.of("password", "Incorrect password for given login")
            );
        }
    }

    private boolean isEmail(String login) {
        return login.contains("@");
    }
}
