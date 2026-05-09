package com.rafkot.chatapp.auth;

import com.rafkot.chatapp.auth.dto.RegistrationResponseDto;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.mapper.UserRegistrationMapper;
import com.rafkot.chatapp.user.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RegistrationControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private UserRegistrationService userRegistrationService;

    @MockitoBean
    private UserRegistrationMapper userRegistrationMapper;

    @Test
    void shouldRegisterUser() {
        // given
        String request = """
            {
                "username": "testuser",
                "email": "testuser@mail.com",
                "password": "testpswd"
            }
        """;

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpswd");

        RegistrationResponseDto registrationResponseDto = new RegistrationResponseDto(
                "testuser",
                "testuser@mail.com"
        );

        when(userRegistrationService.registerUser(any())).thenReturn(user);
        when(userRegistrationMapper.toRegistrationResponseDto(user)).thenReturn(registrationResponseDto);

        // when + then
        assertThat(mockMvcTester.post()
                .uri("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("""
                        {
                            "username": "testuser",
                            "email": "testuser@mail.com"
                        }
                """);
    }
}