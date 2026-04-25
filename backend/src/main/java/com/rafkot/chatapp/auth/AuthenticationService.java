package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.AuthenticationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponseDto authenticate(
            final AuthenticationRequestDto request
    ) {
        final UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken
                .unauthenticated(request.username(), request.password());

        final Authentication authentication = authenticationManager
                .authenticate(authToken);

        final String token = jwtService.generateToken(authentication);
        return new AuthenticationResponseDto(token);
    }
}
