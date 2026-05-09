package com.rafkot.chatapp.user;

import com.rafkot.chatapp.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username)
        throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(user -> new UserDetailsImpl(
                        user.getId(),
                        user.getPassword(),
                        user.getUsername(),
                        user.getEmail(),
                        Collections.emptySet(),
                        true,
                        true,
                        true,
                        true
                ))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username [%s] not found".formatted(username)
                ));
    }
}
