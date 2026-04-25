package com.rafkot.chatapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration")
class SecurityBeansTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void shouldLoadPasswordEncoder() {
        // given
        String rawPassword = "test";

        // when
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // then
        assertThat(passwordEncoder).isNotNull();
        assertThat(encodedPassword).isNotBlank();
    }

    @Test
    void shouldLoadAuthenticationManager() {
        // given + when
        AuthenticationManager result = authenticationManager;

        // then
        assertThat(result).isNotNull();
    }
}
