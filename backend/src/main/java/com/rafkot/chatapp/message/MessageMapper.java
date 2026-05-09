package com.rafkot.chatapp.message;

import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponseDto mapMessageToDto(Message message) {
        return new MessageResponseDto(
                message.getContent(),
                message.getCreatedDate(),
                message.getModifiedDate(),
                message.getSender().getUsername(),
                message.getRecipient().getUsername(),
                message.getId()
        );
    }

}
