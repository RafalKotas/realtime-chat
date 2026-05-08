package com.rafkot.chatapp.message;

public record SendMessageRequest(String recipientUsername, String content) {}
