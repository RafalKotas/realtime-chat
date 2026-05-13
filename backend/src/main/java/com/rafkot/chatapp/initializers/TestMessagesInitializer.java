package com.rafkot.chatapp.initializers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rafkot.chatapp.user.User;
import com.rafkot.chatapp.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Profile("!test")
@Slf4j
@Component
@Order(2)
public class TestMessagesInitializer implements CommandLineRunner {

    private final MessageRawRepository messageRawRepository;
    private final UserRepository userRepository;

    private static final Path USERNAMES_FILE =
            Path.of("src/main/resources/generated-users.json");

    private static final String CONVERSATIONS_DIR = "mock-conversations";

    private static final int CONVERSATIONS_COUNT = 10;

    private static final Random random =  new Random();

    public TestMessagesInitializer(MessageRawRepository messageRawRepository,
                                   UserRepository userRepository) {
        this.messageRawRepository = messageRawRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!Files.exists(USERNAMES_FILE)) {
            log.warn("Usernames file {} not found, skipping message generation.", USERNAMES_FILE);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<String> usernames = mapper.readValue(
                USERNAMES_FILE.toFile(),
                new TypeReference<>() {}
        );

        List<User> users = userRepository.findAllByUsernameIn(usernames);

        if (users.size() != usernames.size()) {
            log.error("Some users from {} are missing in DB (expected {}, found {}). Deleting file and aborting.",
                    USERNAMES_FILE, usernames.size(), users.size());
            Files.deleteIfExists(USERNAMES_FILE);
            return;
        }

        users.sort(Comparator.comparing(User::getUsername));

        log.info("Deleting all existing messages...");
        messageRawRepository.deleteAll();

        List<List<MockMessageEntry>> templates = loadMockConversations(mapper);

        if (templates.isEmpty()) {
            log.warn("No mock conversations found in {}, aborting.", CONVERSATIONS_DIR);
            return;
        }

        int userCount = users.size();
        int pairsCount = userCount * (userCount - 1) / 2;
        int convCount = templates.size();
        int usesPerConversation = pairsCount / convCount;

        log.info("Users: {}, pairs: {}, templates: {}, usesPerTemplate: {}",
                userCount, pairsCount, convCount, usesPerConversation);

        Map<Integer, Integer> usageMap = buildUsageMap(convCount, usesPerConversation);

        List<UserPair> pairs = generateUserPairs(users);

        List<MessageRaw> allMessages = new ArrayList<>();

        for (UserPair pair : pairs) {
            int convId = pickConversation(usageMap);
            List<MockMessageEntry> template = templates.get(convId);

            List<MessageRaw> msgs = mapConversationToMessages(template, pair.u1(), pair.u2());
            allMessages.addAll(msgs);
        }

        messageRawRepository.saveAll(allMessages);
        log.info("Saved {} messages for {} pairs.", allMessages.size(), pairs.size());
    }

    private List<List<MockMessageEntry>> loadMockConversations(ObjectMapper mapper) throws Exception {
        List<List<MockMessageEntry>> conversations = new ArrayList<>();

        ClassLoader cl = getClass().getClassLoader();

        for (int i = 1; i <= CONVERSATIONS_COUNT; i++) {
            String filename = CONVERSATIONS_DIR + "/conversation_" + i + ".json";
            InputStream is = cl.getResourceAsStream(filename);
            if (is == null) {
                log.warn("Conversation file {} not found on classpath.", filename);
                continue;
            }

            List<MockMessageEntry> entries = mapper.readValue(
                    is,
                    new TypeReference<List<MockMessageEntry>>() {}
            );
            conversations.add(entries);
            log.info("Loaded {} messages from {}", entries.size(), filename);
        }

        return conversations;
    }

    private Map<Integer, Integer> buildUsageMap(int conversationCount, int usesPerConversation) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < conversationCount; i++) {
            map.put(i, usesPerConversation);
        }
        return map;
    }

    private int pickConversation(Map<Integer, Integer> usageMap) {
        List<Integer> available = usageMap.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .toList();

        if (available.isEmpty()) {
            throw new IllegalStateException("No available conversations left to assign.");
        }

        int chosen = available.get(TestMessagesInitializer.random.nextInt(available.size()));
        usageMap.put(chosen, usageMap.get(chosen) - 1);
        return chosen;
    }

    private List<MessageRaw> mapConversationToMessages(
            List<MockMessageEntry> template,
            User u1,
            User u2
    ) {
        List<MessageRaw> result = new ArrayList<>();
        int userId;

        for (MockMessageEntry entry : template) {
            userId = entry.id();

            MessageRaw m = new MessageRaw();
            m.setId(UUID.randomUUID());
            m.setContent(entry.content());

            Instant created = entry.createdDate().atZone(ZoneId.systemDefault()).toInstant();
            m.setCreatedDate(created);
            m.setModifiedDate(created);

            m.setSender(userId == 1 ? u1 : u2);
            m.setRecipient(userId == 1 ? u2 : u1);

            result.add(m);
        }

        return result;
    }

    private List<UserPair> generateUserPairs(List<User> users) {
        List<UserPair> pairs = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                pairs.add(new UserPair(users.get(i), users.get(j)));
            }
        }

        return pairs;
    }

    private record UserPair(User u1, User u2) {}
}
