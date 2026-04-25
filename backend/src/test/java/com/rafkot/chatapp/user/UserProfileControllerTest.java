package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.mapper.UserMapper;
import com.rafkot.chatapp.user.dto.UserProfileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserProfileControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnUserProfile() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@mail.com");

        UserProfileDto dto = new UserProfileDto(
                "testuser",
                "testuser@mail.com"
        );

        when(userService.getUserByUsername("testuser")).thenReturn(user);
        when(userMapper.toUserProfileDto(user)).thenReturn(dto);

        // when + then
        assertThat(mockMvcTester.get()
                .uri("/api/user/me")
                .accept(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("""
                    {
                      "email": "testuser@mail.com",
                      "username": "testuser"
                    }
                """);

        verify(userService).getUserByUsername("testuser");
        verify(userMapper).toUserProfileDto(user);
    }

    @Test
    void shouldReturn401WhenPrincipalIsNull() {
        assertThat(mockMvcTester.get()
                .uri("/api/user/me")
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(401);
    }

    @Test
    void shouldReturnUnauthorizedWhenUsernameIsNull() {
        assertThat(mockMvcTester.get()
                .uri("/api/user/me"))
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldThrowUnauthorizedWhenUsernameIsNull_directCall() {
        // given
        UserProfileController controller =
                new UserProfileController(userService, userMapper);

        // when + then
        assertThatThrownBy(() -> controller.getUserProfile(null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401");
    }
}