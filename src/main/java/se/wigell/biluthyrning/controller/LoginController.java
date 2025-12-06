package se.wigell.biluthyrning.controller;

import org.springframework.web.bind.annotation.*;
import se.wigell.biluthyrning.service.UserService;
import se.wigell.biluthyrning.model.User;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User login(@RequestParam String username, @RequestParam String password) {

        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("USERNAME: " + username);
        System.out.println("PASSWORD: " + password);

        return userService.validateLogin(username, password);
    }



}
