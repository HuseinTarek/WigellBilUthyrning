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
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String role = user.getRole() != null ? user.getRole() : "ROLE_USER";

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(role));

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

    public User saveUser(User user) {

        String password = user.getPassword();

        if (password != null && !password.startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return userRepository.save(user);
    }


    public User validateLogin(String username, String password) {

        if (username == null || password == null) return null;

        User user = userRepository.findByUsername(username);
        if (user == null || user.getPassword() == null) return null;

        boolean ok;

        if (user.getPassword().startsWith("$2")) {
            ok = passwordEncoder.matches(password, user.getPassword());
        } else {
            ok = password.equals(user.getPassword());
        }

        if (ok) {
            return user;
        }
        return null;
    }


    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User getByUsername(String name) {
        return userRepository.findByUsername(name);
    }
}
