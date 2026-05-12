package com.rafkot.chatapp.user.mapper;

import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class UserMapper {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneId.systemDefault()).withLocale(Locale.ENGLISH);

    public UserProfileDto toUserProfileDto(User user) {
        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                formatter.format(user.getCreatedDate()),
                formatter.format(user.getModifiedDate())
        );
    }
}
