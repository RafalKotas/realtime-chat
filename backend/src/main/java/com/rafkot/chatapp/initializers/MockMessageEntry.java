package com.rafkot.chatapp.initializers;

import java.time.LocalDateTime;

public record MockMessageEntry(String content, LocalDateTime createdDate, int id) {
}
