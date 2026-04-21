package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.GONE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(GONE,
                        "The user account has been deleted or inactivated"));
    }

    @Component
    public class UserMapper {
        public UserProfileDto toUserProfileDto(final User user) {
            return new UserProfileDto(user.getEmail(), user.getUsername());
        }
    }
}
