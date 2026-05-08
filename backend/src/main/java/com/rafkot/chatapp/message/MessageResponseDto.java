package com.rafkot.chatapp.message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponseDto(
        String content,
        Instant createdDate,
        Instant modifiedDate,
        String senderUsername,
        String receiverUsername,
        UUID messageId
) {
}
