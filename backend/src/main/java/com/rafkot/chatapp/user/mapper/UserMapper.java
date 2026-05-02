package com.rafkot.chatapp.user.mapper;

import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserProfileDto toUserProfileDto(User user) {
        return new UserProfileDto(
                user.getUsername(),
                user.getEmail()
        );
    }
}
