package se.wigell.biluthyrning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.wigell.biluthyrning.model.User;


public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
