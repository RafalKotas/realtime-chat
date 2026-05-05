package com.rafkot.chatapp.message;

import com.rafkot.chatapp.config.UserDetailsImpl;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserService userService;

    public void sendMessage(SendMessageRequest request, Principal principal) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) ((Authentication) principal).getPrincipal();

        UUID senderId = userDetails.getId();

        // TODO - implement custom exception
        if (senderId.equals(request.recipientId())) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }

        User senderUser = userService.getUserById(senderId);
        User recipient = userService.getUserById(request.recipientId());


        Message message = new Message();
        message.setSender(senderUser);
        message.setContent(request.content());
        message.setRecipient(recipient);

        Message saved = save(message);

        messagingTemplate.convertAndSendToUser(
                message.getRecipient().getId().toString(),
                "/queue/messages",
                saved
        );

        messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/messages",
                saved
        );
    }

    private Message save(Message message) {
        return messageRepository.save(message);
    }
}
