CREATE TABLE messages
(
    id           CHAR(36) PRIMARY KEY,
    content      VARCHAR(255) NOT NULL,
    sender_id    CHAR(36) NOT NULL,
    recipient_id CHAR(36) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_sender_user_id FOREIGN KEY(sender_id) REFERENCES users(id),
    CONSTRAINT fk_recipient_user_id FOREIGN KEY(recipient_id) REFERENCES users(id)
);