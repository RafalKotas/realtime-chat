package com.rafkot.chatapp.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService subject;

    private JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        jwtEncoder = mock(JwtEncoder.class);
        subject = new JwtService(
                "test-issuer",
                Duration.ofMinutes(15),
                jwtEncoder
        );
    }

    @Test
    void shouldGenerateTokenWithCorrectClaims() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");

        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("sub", "testuser")
                .build();

        when(jwtEncoder.encode(any())).thenReturn(jwt);

        ArgumentCaptor<JwtEncoderParameters> captor =
                ArgumentCaptor.forClass(JwtEncoderParameters.class);

        // when
        String result = subject.generateToken(authentication);

        // then
        assertThat(result).isEqualTo("test-token");

        verify(jwtEncoder).encode(captor.capture());

        JwtClaimsSet claims = captor.getValue().getClaims();

        assertThat(claims.getSubject()).isEqualTo("testuser");
        assertThat((String) claims.getClaim("iss"))
                .isEqualTo("test-issuer");
        assertThat(claims.getExpiresAt()).isAfter(Instant.now());
    }

    @Test
    void shouldGenerateTokenForUsername() {
        // given
        String username = "rafal";
        String expectedToken = "mocked-jwt-token";

        Instant now = Instant.now();
        Instant expires = now.plus(Duration.ofHours(1));

        Map<String, Object> headers = Map.of("alg", "HS256");
        Map<String, Object> claims = Map.of(
                "sub", username,
                "iss", "my-issuer",
                "exp", expires.getEpochSecond()
        );

        Jwt jwt = new Jwt(
                expectedToken,
                now,
                expires,
                headers,
                claims
        );

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        // when
        String token = subject.generateToken(username);

        // then
        assertThat(token).isEqualTo(expectedToken);
    }
}