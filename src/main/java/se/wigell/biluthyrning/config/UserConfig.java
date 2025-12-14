package se.wigell.biluthyrning.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.wigell.biluthyrning.model.User;
import se.wigell.biluthyrning.repository.UserRepository;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner loadUsers(UserRepository userRepository) {
        return args -> {

            createIfMissing(userRepository,  "user1");
            createIfMissing(userRepository, "user2");
            createIfMissing(userRepository,  "user3");
            createIfMissing(userRepository,  "user4");
            createIfMissing(userRepository, "user6");

            System.out.println("Users loaded.");
        };
    }

    private void createIfMissing(UserRepository repo, String username) {

        String email = username + "@mail.com";

        if (repo.findByEmail(email).isPresent()) return;
        if (repo.findByUsername(username) != null) return;

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setFirstName(username);
        u.setLastName("Test");
        u.setNoOfOrders(0);
        u.setPassword("pass123");
        u.setPhone("0700000000");
        u.setRole("USER");

        repo.save(u);
    }
}
