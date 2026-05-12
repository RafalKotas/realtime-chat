package com.rafkot.chatapp.user;

import com.rafkot.chatapp.auth.dto.ChangePasswordRequestDto;
import com.rafkot.chatapp.auth.dto.ChangePasswordResponseDto;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import com.rafkot.chatapp.user.exception.UserValidationException;
import com.rafkot.chatapp.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null || jwt.getClaim("sub") == null) {
            throw new UserValidationException(HttpStatus.UNAUTHORIZED,
                    Map.of("authentication", "username is null"));
        }

        String username = jwt.getClaim("sub");

        final User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(userMapper.toUserProfileDto(user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponseDto> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ChangePasswordRequestDto dto
    ) {
        if (jwt == null || jwt.getClaim("sub") == null) {
            throw new UserValidationException(HttpStatus.UNAUTHORIZED,
                    Map.of("authentication", "username is null"));
        }

        String username = jwt.getClaim("sub");

        userService.changePassword(username, dto.password(), dto.confirmPassword());

        return ResponseEntity.ok(new ChangePasswordResponseDto("Password changed correctly."));
    }
}
