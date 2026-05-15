package com.rafkot.chatapp.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("""
       SELECT DISTINCT
           CASE
               WHEN m.sender.id = :userId THEN m.recipient.username
               ELSE m.sender.username
           END
       FROM Message m
       WHERE m.sender.id = :userId OR m.recipient.id = :userId
    """)
    List<String> findConversationPartners(UUID userId);

    @Query("""
       SELECT m
       FROM Message m
       WHERE (m.sender.id = :userId AND m.recipient.username = :partnerUsername)
          OR (m.sender.username = :partnerUsername AND m.recipient.id = :userId)
   """)
    List<Message> findConversationMessages(UUID userId, String partnerUsername);

    @Query("""
        SELECT DISTINCT m.sender.id, m.recipient.id
        FROM Message m
    """)
    List<Object[]> findAllUniquePairs();
}
