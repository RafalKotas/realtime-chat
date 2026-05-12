package com.rafkot.chatapp.auth.dto;

public record ChangePasswordRequestDto(String password, String confirmPassword) {
}
