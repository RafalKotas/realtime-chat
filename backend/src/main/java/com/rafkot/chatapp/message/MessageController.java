package com.rafkot.chatapp.message;

import com.rafkot.chatapp.user.exception.UserValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@Slf4j
public class MessageController {

    MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/all/{uuid}")
    public ResponseEntity<Map<String, List<MessageResponseDto>>> getUserMessages(
            @AuthenticationPrincipal Jwt jwt, @PathVariable String uuid) {

        String username = jwt.getClaim("sub");

        if (username == null) {
            throw new UserValidationException(HttpStatus.UNAUTHORIZED, Map.of("authentication", "username is null"));
        }

        Map<String, List<MessageResponseDto>> userGroupedMessages = messageService.getGroupedUserChatsMessages(uuid);

        return ResponseEntity.ok(userGroupedMessages);
    }

    @GetMapping("/{userId}/{partnerUsername}")
    public ResponseEntity<List<MessageResponseDto>> getMessagesWithGivenUserId(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID userId, @PathVariable String partnerUsername) {

        log.info("Fetching messages for user {} with partner {}", userId, partnerUsername);
        String username = jwt.getClaim("sub");

        if (username == null) {
            throw new UserValidationException(HttpStatus.UNAUTHORIZED, Map.of("authentication", "username is null"));
        }

        List<MessageResponseDto> messages = messageService.getMessagesBetweenUsers(userId, partnerUsername);

        return ResponseEntity.ok(messages);
    }
}
