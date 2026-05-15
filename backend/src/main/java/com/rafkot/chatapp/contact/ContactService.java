package com.rafkot.chatapp.contact;

import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import com.rafkot.chatapp.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactService {

    UserRepository userRepository;

    ContactRepository contactRepository;

    ContactMapper contactMapper;

    public ContactService(UserRepository userRepository, ContactRepository contactRepository, ContactMapper contactMapper) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    public List<ContactDto> getContactsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<Contact> contacts = contactRepository.findAllByOwner(user);

        return contacts.stream()
                .map(contactMapper::toContactDto)
                .toList();
    }

    public void deleteContact(UUID contactId, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Contact contact = contactRepository.findByIdAndOwner(contactId, owner)
                .orElseThrow(() -> new ContactNotFoundException(contactId));

        contactRepository.delete(contact);
    }
}
