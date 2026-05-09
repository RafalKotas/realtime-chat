package com.rafkot.chatapp.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Slf4j
@Component
@Order(1)
public class TestUsersInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final List<String> FIRST_NAMES =
            List.of("Adam", "Alice", "Bob", "Charlie");

    private static final List<String> LAST_NAMES =
            List.of("Smith", "Doe", "Carlson", "Reynolds");

    private static final int REQUIRED_USERS_COUNT = 16;

    private static final Path USERNAMES_FILE =
            Path.of("src/main/resources/generated-users.json");

    public TestUsersInitializer(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        long existing = userRepository.count();

        if (existing >= REQUIRED_USERS_COUNT) {
            log.info("Users already exist, skipping generation.");
            return;
        }

        log.info("Generating {} test users...", REQUIRED_USERS_COUNT);

        List<User> users = generateUsers();
        userRepository.saveAll(users);

        saveUsernamesToJson(users);

        log.info("Generated and saved {} users.", users.size());
    }

    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();

        for (String first : FIRST_NAMES) {
            for (String last : LAST_NAMES) {
                User user = new User();
                String username = first.toLowerCase() + "_" + last.toLowerCase();
                String encoded = passwordEncoder.encode(username);

                user.setUsername(username);
                user.setPassword(encoded);
                user.setEmail(username + "@mail.com");

                users.add(user);
                log.info("Generated user: {}", username);
            }
        }

        return users;
    }

    private void saveUsernamesToJson(List<User> users) throws Exception {
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .toList();

        ObjectMapper mapper = new ObjectMapper();

        Files.createDirectories(USERNAMES_FILE.getParent());
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(USERNAMES_FILE.toFile(), usernames);

        log.info("Saved usernames to {}", USERNAMES_FILE);
    }
}