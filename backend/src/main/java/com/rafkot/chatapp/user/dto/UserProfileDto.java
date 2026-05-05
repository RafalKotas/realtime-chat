package com.rafkot.chatapp.user.dto;

import java.util.UUID;

public record UserProfileDto(UUID id, String username, String email) {
}
