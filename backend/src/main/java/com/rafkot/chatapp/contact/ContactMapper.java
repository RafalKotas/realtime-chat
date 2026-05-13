package com.rafkot.chatapp.contact;

import com.rafkot.chatapp.user.User;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactDto toContactDto(Contact contact) {
        User c = contact.getMember();

        return new ContactDto(
                contact.getId(),
                c.getUsername(),
                null // avatar
        );
    }
}
