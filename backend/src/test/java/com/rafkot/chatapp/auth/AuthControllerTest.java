package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.LoginResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    void shouldAuthenticateUser() {
        // given
        String request = """
            {
              "username": "testuser",
              "password": "password"
            }
        """;

        LoginResponseDto response = new LoginResponseDto("access-token-123", "refresh-token-123");

        when(authenticationService.authenticate(any()))
                .thenReturn(response);

        // when + then
        assertThat(mockMvcTester.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("""
                    {
                      "accessToken": "access-token-123",
                      "refreshToken": "refresh-token-123"
                    }
                """);
    }
}