package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.LoginResponseDto;
import com.rafkot.chatapp.auth.dto.RefreshResponseDto;
import com.rafkot.chatapp.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(
            @RequestBody final AuthenticationRequestDto authenticationRequestDto
    ) {
        log.info("Trying to authenticate user: {}", authenticationRequestDto);

        LoginResponseDto loginResponse = authenticationService.authenticate(authenticationRequestDto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", loginResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .partitioned(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60L)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        // refreshToken is source of truth
        log.info("Request for new access token: validating refresh token...");
        User user = refreshTokenService.validateRefreshToken(refreshToken);

        log.info("Refresh token validated successfully");
        String newAccessToken = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(new RefreshResponseDto(newAccessToken));
    }
}
