package com.rafkot.chatapp.message;

import com.rafkot.chatapp.config.JwtConfig;
import com.rafkot.chatapp.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Import({SecurityConfig.class, JwtConfig.class})
@AutoConfigureMockMvc
@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private MessageService messageService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @WithMockUser(username = "testUser1")
    void shouldReturnUserGroupedMessages() {
        // given
        String uuid = "11111111-2222-3333-4444-555555555555";
        Map<String, List<MessageResponseDto>> userGroupedMessages = new HashMap<>();
        Instant t1 = Instant.parse("2026-05-08T19:01:42.916334400Z");
        Instant t2 = Instant.parse("2026-05-08T19:01:57.916334400Z");
        Instant t3 = Instant.parse("2026-05-08T19:02:12.916334400Z");
        Instant t4 = Instant.parse("2026-05-08T19:02:23.916334400Z");

        // testUser1, testUser2
        MessageResponseDto messageResponseDto1send2rec01 = new MessageResponseDto(
                "Hello world 121!",
                t1,
                t1,
                "testUser1",
                "testUser2",
                UUID.fromString("00011111-1111-1111-1111-000000011111")
                );
        MessageResponseDto messageResponseDto2send1rec02 = new MessageResponseDto(
                "Hello world 122!",
                t2,
                t2,
                "testUser2",
                "testUser1",
                UUID.fromString("00022222-2222-2222-2222-000000022222")
        );
        List<MessageResponseDto> messageResponseDtoListTestUser1TestUser2 = new ArrayList<>(
                List.of(messageResponseDto1send2rec01, messageResponseDto2send1rec02)
        );

        userGroupedMessages.put("testUser2", messageResponseDtoListTestUser1TestUser2);

        // testUser1, testUser3
        MessageResponseDto messageResponseDto1send3rec01 = new MessageResponseDto(
                "Hello world 131!",
                t3,
                t3,
                "testUser1",
                "testUser3",
                UUID.fromString("00033333-3333-3333-3333-000000033333")
        );
        MessageResponseDto messageResponseDto3send1rec02 = new MessageResponseDto(
                "Hello world 132!",
                t4,
                t4,
                "testUser3",
                "testUser1",
                UUID.fromString("00044444-4444-4444-4444-000000044444")
        );
        List<MessageResponseDto> messageResponseDtoListTestUser1TestUser3 = new ArrayList<>(
                List.of(messageResponseDto1send3rec01, messageResponseDto3send1rec02)
        );

        userGroupedMessages.put("testUser3", messageResponseDtoListTestUser1TestUser3);

        when(messageService.getGroupedUserChatsMessages(uuid)).thenReturn(userGroupedMessages);

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "testUser1")
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        // when + then
        mockMvcTester.get()
                .uri("/api/message/all/" + uuid)
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .isStrictlyEqualTo("""
                        {
                            "testUser3": [
                                {
                                    "content": "Hello world 131!",
                                    "createdDate": "2026-05-08T19:02:12.916334400Z",
                                    "modifiedDate": "2026-05-08T19:02:12.916334400Z",
                                    "senderUsername": "testUser1",
                                    "receiverUsername": "testUser3",
                                    "messageId": "00033333-3333-3333-3333-000000033333"
                                },
                                {
                                    "content": "Hello world 132!",
                                    "createdDate": "2026-05-08T19:02:23.916334400Z",
                                    "modifiedDate": "2026-05-08T19:02:23.916334400Z",
                                    "senderUsername": "testUser3",
                                    "receiverUsername": "testUser1",
                                    "messageId": "00044444-4444-4444-4444-000000044444"
                                }
                            ],
                            "testUser2":[
                                {
                                    "content": "Hello world 121!",
                                    "createdDate": "2026-05-08T19:01:42.916334400Z",
                                    "modifiedDate": "2026-05-08T19:01:42.916334400Z",
                                    "senderUsername": "testUser1",
                                    "receiverUsername": "testUser2",
                                    "messageId": "00011111-1111-1111-1111-000000011111"
                                },
                                {
                                    "content": "Hello world 122!",
                                    "createdDate": "2026-05-08T19:01:57.916334400Z",
                                    "modifiedDate": "2026-05-08T19:01:57.916334400Z",
                                    "senderUsername": "testUser2",
                                    "receiverUsername": "testUser1",
                                    "messageId": "00022222-2222-2222-2222-000000022222"
                                }
                            ]
                        }
                """);

        verify(messageService).getGroupedUserChatsMessages(uuid);
    }

    @Test
    @WithMockUser(username = "testUser1")
    void shouldThrowExceptionWhenJwtIsNull() {
        // given
        String uuid = "11111111-2222-3333-4444-555555555555";

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("test-claim", "test-value")
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        // when + then
        assertThat(mockMvcTester.get()
                .uri("/api/message/all/" + uuid)
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.UNAUTHORIZED.value())
                .bodyJson()
                .isLenientlyEqualTo("""
                {
                  "authentication": "username is null"
                }
            """);
    }
}