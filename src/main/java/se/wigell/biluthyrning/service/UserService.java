package se.wigell.biluthyrning.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.wigell.biluthyrning.repository.UserRepository;
import se.wigell.biluthyrning.model.User;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        return userRepository.save(user);
    }

    public User validateLogin(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
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
