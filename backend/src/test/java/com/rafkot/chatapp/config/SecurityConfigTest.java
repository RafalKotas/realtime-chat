package com.rafkot.chatapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldNotAllowLoginEndpointWhenUserNotExists() throws Exception {
        // given
        String apiLoginEndpoint = "/api/auth/login";
        String loginRequestContent = """
                            {
                                "login": "nonExistingUser",
                                "password": "passwordForNonExistingUser"
                            }
                        """;

        // when + then
        mockMvc.perform(post(apiLoginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestContent))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRequireAuthenticationForApiEndpoints() throws Exception {
        // given
        String apiUserProfileEndpoint = "/api/user/me";

        // when + then
        mockMvc.perform(get(apiUserProfileEndpoint))
                .andExpect(status().isUnauthorized());
    }
}