package com.rafkot.chatapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void send(SendMessageRequest request, Principal principal) {
        messageService.sendMessage(request, principal);
    }
}
