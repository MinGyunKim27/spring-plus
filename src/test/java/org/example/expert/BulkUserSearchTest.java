package org.example.expert;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BulkUserSearchTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void 셋업(){
        if (userRepository.count() >= 1_000_000) return;

        List<User> users = new ArrayList<>();

        users.add(new User("testuser@email.com", "1234", UserRole.USER, "user_abc12345"));
        for (int i = 0; i<999_999; i++){
            String nickname = "user_" + UUID.randomUUID();
            String email = "user@" + UUID.randomUUID();
            users.add(new User(email,"1313", UserRole.USER,nickname));
        }

        for (int i = 0; i < users.size(); i += 10000) {
            int end = Math.min(i + 10000, users.size());
            userRepository.saveAll(users.subList(i, end));
        }
    }

    @Test
    void searchTest() {
        long start = System.nanoTime();

        Optional<User> found = userRepository.findByNickname("user_abc12345");

        long end = System.nanoTime();
        long elapsedMs = (end - start) / 1_000_000;
        System.out.println("검색 소요 시간: " + elapsedMs + "ms");

        assertTrue(found.isPresent());
    }
}
