package com.rafkot.chatapp.message;

import com.rafkot.chatapp.user.exception.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
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
}
