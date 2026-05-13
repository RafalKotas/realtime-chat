package com.rafkot.chatapp.contact;

import java.util.UUID;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(UUID id) {
        super("Contact not found: " + id);
    }
}

