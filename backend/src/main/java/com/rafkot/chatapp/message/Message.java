package com.rafkot.chatapp.message;

import com.rafkot.chatapp.common.AuditableEntity;
import com.rafkot.chatapp.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Message extends AuditableEntity {

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    User recipient;

    @Column(name = "content", length = -1)
    private String content;
}
