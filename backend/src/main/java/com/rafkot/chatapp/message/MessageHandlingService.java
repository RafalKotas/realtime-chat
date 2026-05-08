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

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageHandlingService {

    private final MessageRepository messageRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserService userService;
    private final MessageMapper messageMapper;

    public void sendMessage(SendMessageRequest request, Principal principal) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) ((Authentication) principal).getPrincipal();

        String senderUsername =  userDetails.getUsername();

        // TODO - implement custom exception
        if (senderUsername.equals(request.recipientUsername())) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }

        User senderUser = userService.getUserByUsername(senderUsername);
        User recipient =  userService.getUserByUsername(request.recipientUsername());


        Message message = new Message();
        message.setSender(senderUser);
        message.setContent(request.content());
        message.setRecipient(recipient);

        Message saved = save(message);

        MessageResponseDto messageResponseDto = messageMapper.mapMessageToDto(saved);

        messagingTemplate.convertAndSendToUser(
                message.getRecipient().getUsername(),
                "/queue/messages",
                messageResponseDto
        );
    }

    private Message save(Message message) {
        return messageRepository.save(message);
    }
}
