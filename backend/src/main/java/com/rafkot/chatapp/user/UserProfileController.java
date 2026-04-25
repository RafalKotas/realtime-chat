package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.mapper.UserMapper;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(userMapper.toUserProfileDto(user));
    }
}
