package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final UserService.UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(
            final Authentication authentication) {

        final User user = userService.getUserByUsername(authentication.getName());

        return ResponseEntity.ok(userMapper.toUserProfileDto(user));
    }
}
