package com.rafkot.chatapp.security.config;

import com.rafkot.chatapp.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtConfigTest {

    private JwtConfig subject;

    @BeforeEach
    void setUp() {
        subject = new JwtConfig();
        subject.setTtl(Duration.ofMinutes(10));

        subject.setPrivateKey(new ClassPathResource("jwt/private.pem"));
        subject.setPublicKey(new ClassPathResource("jwt/public.pem"));
    }

    @Test
    void shouldCreateJwtEncoder() {
        // when
        JwtEncoder encoder = subject.jwtEncoder();

        // then
        assertThat(encoder).isNotNull();
    }

    @Test
    void shouldCreateJwtDecoder() {
        // given

        // when
        JwtDecoder decoder = subject.jwtDecoder();

        // then
        assertThat(decoder).isNotNull();
    }

    @Test
    void shouldCreateJwtService() {
        // given
        JwtEncoder encoder = subject.jwtEncoder();

        // when
        JwtService service = subject.jwtService("test-app", encoder);

        // then
        assertThat(service).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenPublicKeyIsInvalid() {
        // given
        subject.setPublicKey(new ClassPathResource("jwt/invalid.pem"));

        // when + then
        assertThatThrownBy(() -> subject.jwtDecoder())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot load public key");
    }

    @Test
    void shouldThrowExceptionWhenPrivateKeyIsInvalid() {
        // given
        subject.setPrivateKey(new ClassPathResource("jwt/invalid.pem"));

        // when + then
        assertThatThrownBy(() -> subject.jwtEncoder())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot load private key");
    }
}