package com.rafkot.chatapp.security.config;

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
    void shouldAllowAuthEndpointsWithoutAuthentication() throws Exception {
        // given
        String apiLoginEndpoint = "/api/auth/login";
        String loginRequestContent = """
                            {
                                "username": "test",
                                "password": "test"
                            }
                        """;

        // when + then
        mockMvc.perform(post(apiLoginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestContent))
                .andExpect(status().isForbidden());
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