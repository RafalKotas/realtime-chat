package com.rafkot.chatapp.contact;

import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import com.rafkot.chatapp.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    ContactService subject;

    @Mock
    UserRepository userRepository;

    @Mock
    ContactRepository contactRepository;

    @Mock
    ContactMapper contactMapper;

    @BeforeEach
    void setUp() {
        subject = new ContactService(userRepository, contactRepository, contactMapper);
    }

    @Test
    void shouldGetContactsForUser() {
        // given
        String username = "test-username";
        User testUser = new User();
        testUser.setUsername(username);

        User testUserContactUser1 = new User();
        String testUserContact1username = "test-username-contact-1";
        testUserContactUser1.setUsername(testUserContact1username);

        User testUserContactUser2 = new User();
        String testUserContact2username = "test-username-contact-2";
        testUserContactUser2.setUsername(testUserContact2username);

        User testUserContactUser3 = new User();
        String testUserContact3username = "test-username-contact-3";
        testUserContactUser3.setUsername(testUserContact3username);

        Contact contact1 = new Contact();
        contact1.setId(UUID.fromString("11111-22222-33333-44444-55551"));
        contact1.setMember(testUserContactUser1);
        ContactDto contactDto1 = new ContactDto(
                UUID.fromString("11111-22222-33333-44444-55551"),
                "test-username-contact-1",
                null);

        Contact contact2 = new Contact();
        contact2.setId(UUID.fromString("11111-22222-33333-44444-55552"));
        contact2.setMember(testUserContactUser2);
        ContactDto contactDto2 = new ContactDto(
                UUID.fromString("11111-22222-33333-44444-55552"),
                "test-username-contact-2",
                null);

        Contact contact3 = new Contact();
        contact3.setId(UUID.fromString("11111-22222-33333-44444-55553"));
        contact3.setMember(testUserContactUser3);
        ContactDto contactDto3 = new ContactDto(
                UUID.fromString("11111-22222-33333-44444-55553"),
                "test-username-contact-3",
                null);

        List<Contact> contacts = new ArrayList<>(List.of(contact1, contact2, contact3));

        when(userRepository.findByUsername("test-username")).thenReturn(Optional.of(testUser));

        when(contactRepository.findAllByOwner(testUser)).thenReturn(contacts);
        when(contactMapper.toContactDto(contact1)).thenReturn(contactDto1);
        when(contactMapper.toContactDto(contact2)).thenReturn(contactDto2);
        when(contactMapper.toContactDto(contact3)).thenReturn(contactDto3);

        // when
        List<ContactDto> result = subject.getContactsForUser("test-username");

        // then
        assertThat(result)
                .isNotNull()
                .hasSize(3);
        assertThat(result.getFirst().username()).isEqualTo("test-username-contact-1");
        assertThat(result.getFirst().id()).isEqualTo(UUID.fromString("11111-22222-33333-44444-55551"));
        assertThat(result.get(1).username()).isEqualTo("test-username-contact-2");
        assertThat(result.get(1).id()).isEqualTo(UUID.fromString("11111-22222-33333-44444-55552"));
        assertThat(result.get(2).username()).isEqualTo("test-username-contact-3");
        assertThat(result.get(2).id()).isEqualTo(UUID.fromString("11111-22222-33333-44444-55553"));
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionIfUserWithGivenUsernameNotFound() {
        // given
        String username = "test-username";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> subject.getContactsForUser("test-username")
        );

        assertThat(ex.getMessage()).isEqualTo("User with name test-username not found");
    }

    @Test
    void shouldDeleteContactByIdAndOwner() {
        // given
        String username = "test-username";

        User testUser = new User();
        testUser.setUsername(username);


        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setOwner(testUser);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(testUser));

        when(contactRepository.findByIdAndOwner(contactId, testUser))
                .thenReturn(Optional.of(contact));

        // when
        subject.deleteContact(contactId, username);

        // then
        verify(contactRepository).delete(contact);
    }

    @Test
    void shouldThrowUserNotFoundExceptionIfContactWithGivenUsernameNotFound() {
        // given
        String username = "test-username";
        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> subject.deleteContact(contactId, username)
        );

        assertThat(ex.getMessage()).isEqualTo("User with name test-username not found");
    }

    @Test
    void shouldThrowContactNotFoundExceptionIfContactWithGivenOwnerNotFound() {
        // given
        String username = "test-username";

        User testUser = new User();
        testUser.setUsername(username);

        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // when
        ContactNotFoundException ex = assertThrows(
                ContactNotFoundException.class,
                () -> subject.deleteContact(contactId, username)
        );

        assertThat(ex.getMessage()).isEqualTo("Contact not found: 11111111-2222-3333-4444-555555555555");
    }

    @Test
    void shouldThrowContactNotFoundExceptionWhenContactDoesNotExist() {
        // given
        String username = "testUser";

        User owner = new User();
        owner.setUsername(username);

        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(owner));

        when(contactRepository.findByIdAndOwner(contactId, owner))
                .thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> subject.deleteContact(contactId, username))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessageContaining(contactId.toString());

        verify(contactRepository, never()).delete(any());
    }
}