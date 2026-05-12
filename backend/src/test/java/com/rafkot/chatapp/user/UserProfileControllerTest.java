package com.rafkot.chatapp.user;

import com.rafkot.chatapp.user.dto.UserProfileDto;
import com.rafkot.chatapp.user.exception.UserValidationException;
import com.rafkot.chatapp.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserProfileControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void shouldReturnUserProfile() {
        // given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "testUser")
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);

        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@mail.com");

        UserProfileDto dto = new UserProfileDto(
                "testUser",
                "testUser@mail.com",
                "2024-01-01T00:00:00Z",
                "2024-01-01T00:00:00Z"
        );

        when(userService.getUserByUsername("testUser")).thenReturn(user);
        when(userMapper.toUserProfileDto(user)).thenReturn(dto);

        // when + then
        assertThat(mockMvcTester.get()
                .uri("/api/user/me")
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("""
                {
                  "username": "testUser",
                  "email": "testUser@mail.com"
                }
            """);
    }

    @Test
    void shouldReturn401WhenJwtIsNullUserMe() {
        // given + when + then
        assertThat(mockMvcTester.get()
                .uri("/api/user/me")
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .bodyJson()
                .isLenientlyEqualTo("""
                {
                  "authentication": "username is null"
                }
            """);
    }

    @Test
    void shouldReturn401WhenJwtHasNoSubClaimUserMe() {
        // given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("testClaim", "testValue")
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // when + then
        assertThat(mockMvcTester.get()
                .uri("/api/user/me")
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .bodyJson()
                .isLenientlyEqualTo("""
                {
                  "authentication": "username is null"
                }
            """);
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
                .isInstanceOf(UserValidationException.class);
    }

    @Test
    void shouldReturn401WhenJwtIsNullChangePassword() {
        // given + when + then
        assertThat(mockMvcTester.post()
                .uri("/api/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "password": "pass",
                        "confirmPassword": "pass"
                    }
                """)
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .bodyJson()
                .isLenientlyEqualTo("""
                    {
                        "authentication": "username is null"
                    }
                """);
    }

    @Test
    void shouldReturn401WhenJwtHasNoSubClaimChangePassword() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("testClaim", "testValue")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        assertThat(mockMvcTester.post()
                .uri("/api/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "password": "pass",
                        "confirmPassword": "pass"
                    }
                """)
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .bodyJson()
                .isLenientlyEqualTo("""
                    {
                        "authentication": "username is null"
                    }
                """);
    }

    @Test
    void shouldReturn400WhenPasswordsDoNotMatch() {
        // given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "testUser")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
        doThrow(new IllegalArgumentException("Password do not match"))
                .when(userService)
                .changePassword("testUser", "abc", "xyz");

        // when + then
        assertThat(mockMvcTester.post()
                .uri("/api/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "password": "abc",
                        "confirmPassword": "xyz"
                    }
                """)
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .isLenientlyEqualTo("""
                    {
                        "error": "Password do not match"
                    }
                """);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        // given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "testUser")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        User user = new User();
        user.setUsername("testUser");

        when(userService.getUserByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");

        // when & then
        assertThat(mockMvcTester.post()
                .uri("/api/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "password":"newPass","confirmPassword":"newPass"
                    }
                """)
                .accept(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("""
                    {
                        "message": "Password changed correctly."
                    }
                """);
    }

}