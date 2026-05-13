CREATE TABLE contacts
(
    id           CHAR(36) PRIMARY KEY,
    owner_id     CHAR(36) NOT NULL,
    member_id    CHAR(36) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_owner_id FOREIGN KEY(owner_id) REFERENCES users(id),
    CONSTRAINT fk_member_id FOREIGN KEY(member_id) REFERENCES users(id)
);

CREATE INDEX idx_contacts_owner ON contacts(owner_id);
CREATE INDEX idx_contacts_member ON contacts(member_id);