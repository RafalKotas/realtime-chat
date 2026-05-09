package com.rafkot.chatapp.initializers;

import com.rafkot.chatapp.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "messages")
public class MessageRaw {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "content", length = -1)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdDate;

    @Column(name = "modified_at")
    private Instant modifiedDate;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
}
