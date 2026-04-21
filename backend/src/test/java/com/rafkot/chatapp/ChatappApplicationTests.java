package com.rafkot.chatapp;

import com.rafkot.chatapp.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
class ChatappApplicationTests {

	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private JwtEncoder jwtEncoder;

	@MockitoBean
	private JwtDecoder jwtDecoder;

	@MockitoBean
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

}
