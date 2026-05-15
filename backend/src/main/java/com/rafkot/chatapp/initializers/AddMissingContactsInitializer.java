package com.rafkot.chatapp.initializers;

import com.rafkot.chatapp.contact.Contact;
import com.rafkot.chatapp.contact.ContactRepository;
import com.rafkot.chatapp.message.MessageRepository;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Profile("!test")
@Slf4j
@Component
@Order(4)
public class AddMissingContactsInitializer implements CommandLineRunner {

    private final MessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public AddMissingContactsInitializer(
            MessageRepository messageRepository,
            ContactRepository contactRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        List<Object[]> rawPairs = messageRepository.findAllUniquePairs();

        Set<Pair<UUID, UUID>> uniquePairs = new HashSet<>();

        for (Object[] row : rawPairs) {
            UUID sender = (UUID) row[0];
            UUID recipient = (UUID) row[1];

            UUID first = sender.compareTo(recipient) < 0 ? sender : recipient;
            UUID second = sender.compareTo(recipient) < 0 ? recipient : sender;

            uniquePairs.add(new Pair<>(first, second));
        }

        for (Pair<UUID, UUID> pair : uniquePairs) {
            UUID a = pair.getFirst();
            UUID b = pair.getSecond();

            boolean exists = contactRepository.existsByOwnerIdAndMemberId(a, b)
                    || contactRepository.existsByOwnerIdAndMemberId(b, a);

            if (!exists) {
                Contact contact = new Contact();
                User firstUser = userRepository.findById(a).get();
                User secondUser = userRepository.findById(b).get();
                contact.setOwner(firstUser);
                contact.setMember(secondUser);

                contactRepository.save(contact);
            }
        }

        log.info("Missing contacts added successfully.");
    }

    private static class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() { return first; }
        public B getSecond() { return second; }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair<?, ?> p)) return false;
            return Objects.equals(first, p.first) && Objects.equals(second, p.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}
