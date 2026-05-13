package com.rafkot.chatapp.initializers;

import com.rafkot.chatapp.contact.Contact;
import com.rafkot.chatapp.contact.ContactRepository;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Profile("!test")
@Slf4j
@Component
@Order(3)
public class TestContactsForTestUsersInitializer implements CommandLineRunner {

    private static final List<String> FIRST_NAMES = List.of("Irene", "Benjamin", "Evan", "Jason", "Lisa", "Rebecca", "Melanie", "Stephanie", "Austin", "Frank");
    private static final List<String> LAST_NAMES = List.of("Davies", "Quinn", "Churchill", "Hodges", "McLean", "Kelly", "Rutherford", "Mathis", "Miller", "Graham");
    private static final int MIN_REQUIRED_CONTACTS = 40;

    private static final Random RANDOM = new Random();

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ContactRepository contactRepository;

    public TestContactsForTestUsersInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository, ContactRepository contactRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Generating {} test users for mock contacts...", FIRST_NAMES.size() * LAST_NAMES.size());

        List<User> users = generateUsers();
        userRepository.saveAll(users);

        generateUsersContacts();

    }

    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();

        for  (String userFirstName : FIRST_NAMES) {
            for  (String userLastName : LAST_NAMES) {
                User user = new User();
                String username = userFirstName.toLowerCase() + "_" + userLastName.toLowerCase();
                String encoded = passwordEncoder.encode(username);

                user.setUsername(username);
                user.setEmail(username + "@mail.com");
                user.setPassword(encoded);

                users.add(user);
                log.info("Generated user: {}", username);
            }
        }

        return users;
    }

    private void generateUsersContacts() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            String username = user.getUsername();
            int userContacts = contactRepository.countUserContacts(username);

            if (userContacts <= MIN_REQUIRED_CONTACTS) {
                List<Contact> userDesiredContacts = generateContactsForUser(username, users);
                contactRepository.saveAll(userDesiredContacts);
            }
        }
    }

    private List<Contact> generateContactsForUser(String username, List<User> allUsers) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow();

        int currentContacts = contactRepository.countUserContacts(username);

        if (currentContacts >= MIN_REQUIRED_CONTACTS) {
            return Collections.emptyList();
        }

        List<User> candidates = allUsers.stream()
                .filter(u -> !u.getUsername().equals(username))
                .collect(Collectors.toCollection(ArrayList::new));

        int maxContacts = 100;
        int bound = maxContacts - MIN_REQUIRED_CONTACTS + 1; // ile różnych wartości
        int targetContacts = MIN_REQUIRED_CONTACTS + RANDOM.nextInt(bound);

        List<Contact> result = new ArrayList<>();
        int generated = 0;

        while (generated < targetContacts && !candidates.isEmpty()) {
            int randomIndex = RANDOM.nextInt(candidates.size());
            User contactMember = candidates.get(randomIndex);

            if (contactRepository.existByOwnerAndMember(currentUser.getId(), contactMember.getId())) {
                candidates.remove(randomIndex);
                continue;
            }

            Contact contact = new Contact();
            contact.setOwner(currentUser);
            contact.setMember(contactMember);
            result.add(contact);
            generated++;

            candidates.remove(randomIndex);
        }

        return result;
    }

}
