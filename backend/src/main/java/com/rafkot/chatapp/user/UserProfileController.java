package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.dto.UserProfileDto;
import com.rafkot.chatapp.user.exception.UserValidationException;
import com.rafkot.chatapp.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @AuthenticationPrincipal(expression = "username") String username) {

        if (username == null) {
            throw new UserValidationException(HttpStatus.UNAUTHORIZED, Map.of("authentication", "username is null"));
        }

        final User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(userMapper.toUserProfileDto(user));
    }
}
