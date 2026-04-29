package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.AuthenticationRequestDto;
import com.rafkot.chatapp.auth.dto.LoginResponseDto;
import com.rafkot.chatapp.auth.dto.RefreshRequestDto;
import com.rafkot.chatapp.auth.dto.RefreshResponseDto;
import com.rafkot.chatapp.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody final AuthenticationRequestDto authenticationRequestDto
    ) {
        ResponseEntity<AuthenticationResponseDto> responseEntity;

        log.info("Trying to authenticate user: {}", authenticationRequestDto);

        responseEntity = new ResponseEntity<>(authenticationService.authenticate(authenticationRequestDto), HttpStatus.OK);

        responseEntity.getHeaders().add("Access-Control-Allow-Origin", "*");

        log.info("Response entity: {}", responseEntity.getBody());

        return responseEntity;
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refreshToken(@RequestBody RefreshRequestDto refreshRequestDto) {
        // refreshToken is source of truth
        User user = refreshTokenService.validateRefreshToken(refreshRequestDto.refreshToken());

        String newAccessToken = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(new RefreshResponseDto(newAccessToken));
    }

}
