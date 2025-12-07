package se.wigell.biluthyrning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.wigell.biluthyrning.repository.UserRepository;
import se.wigell.biluthyrning.model.User;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user){
        if (user.getPassword() != null) {
            String pw = user.getPassword();
            if (!pw.startsWith("$2a$") && !pw.startsWith("$2b$") && !pw.startsWith("$2y$")) {
                user.setPassword(passwordEncoder.encode(pw));
            }
        }
        return userRepository.save(user);
    }
//    public User validateLogin(String username, String password) {
//        User user = userRepository.findByUsername(username);
//
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Wrong password");
//        }
//
//        return user;
//    }


//    // validateLogin now returns null for bad credentials instead of throwing RuntimeException
//    public User validateLogin(String username, String rawPassword) {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            return null;
//        }
//        // if passwords are stored hashed, use PasswordEncoder.matches
//        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
//            return null;
//        }
//        return user;
//    }

    public User validateLogin(String username, String rawPassword) {
        if (username == null || rawPassword == null) return null;

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        String stored = user.getPassword();
        if (stored == null) {
            return null;
        }

        // if stored looks like bcrypt hash, use PasswordEncoder.matches
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            if (!passwordEncoder.matches(rawPassword, stored)) {
                return null;
            }
        } else {
            // legacy/plaintext support: compare directly (log a warning)
            log.warn("User '{}' has a non-hashed password in DB. Migrate to bcrypt.", username);
            if (!rawPassword.equals(stored)) {
                return null;
            }
        }

        return user;
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User getByUsername(String name) {
        return userRepository.findByUsername(name);
    }
}
