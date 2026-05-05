package com.rafkot.chatapp.message;

import java.util.UUID;

public record SendMessageRequest(UUID recipientId, String content) {}
