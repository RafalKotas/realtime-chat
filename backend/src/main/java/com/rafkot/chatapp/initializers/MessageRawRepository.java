package com.rafkot.chatapp.initializers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRawRepository extends JpaRepository<MessageRaw, UUID> {
}
