package com.rafkot.chatapp.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageService {

    MessageRepository messageRepository;
    MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public Map<String, List<MessageResponseDto>> getGroupedUserChatsMessages(String userId) {
        UUID userUUID = UUID.fromString(userId);

        List<String> messages = messageRepository.findConversationPartners(userUUID);

        return messages.stream()
                .collect(Collectors.toMap(
                        partnerUsername -> partnerUsername,
                        partnerUsername -> messageRepository
                                .findConversationMessages(userUUID, partnerUsername)
                                .stream()
                                .map(messageMapper::mapMessageToDto)
                                .sorted(Comparator.comparing(MessageResponseDto::createdDate))
                                .toList()
                ));
    }
}
