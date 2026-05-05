package com.rafkot.chatapp.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;

import java.time.Duration;
import java.time.Instant;

public class JwtService {

    private final String issuer;

    private final Duration ttl;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public JwtService(String issuer, Duration ttl, JwtEncoder jwtEncoder,  JwtDecoder jwtDecoder) {
        this.issuer = issuer;
        this.ttl = ttl;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(final Authentication authentication) {
        final var claimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuer(issuer)
                .expiresAt(Instant.now().plus(ttl))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

    public String generateToken(String username) {
        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer(issuer)
                .expiresAt(Instant.now().plus(ttl))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

    public String getUsernameFromToken(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

}
