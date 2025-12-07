package se.wigell.biluthyrning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.wigell.biluthyrning.repository.UserRepository;
import se.wigell.biluthyrning.model.User;

import java.util.Collections;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        String raw = user.getRole() == null ? "USER" : user.getRole().trim();

        java.util.List<SimpleGrantedAuthority> authorities = java.util.Arrays.stream(raw.split("[,;\\s]+"))
                .map(r -> r.trim().toUpperCase())
                .filter(r -> !r.isEmpty())
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r) // remove any existing prefix
                .map(r -> "ROLE_" + r) // ensure single prefix
                .map(SimpleGrantedAuthority::new)
                .collect(java.util.stream.Collectors.toList());

        if (authorities.isEmpty()) {
            authorities = java.util.Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
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
