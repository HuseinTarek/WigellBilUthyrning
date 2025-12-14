package se.wigell.biluthyrning.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.wigell.biluthyrning.model.LoginRequest;
import se.wigell.biluthyrning.service.UserService;
import se.wigell.biluthyrning.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        User user = userService.validateLogin(
                request.getUsername(),
                request.getPassword()
        );

        authenticateUser(user, httpRequest);

        return user;
    }



    private void authenticateUser(User user, HttpServletRequest request) {

        // Create role
        SimpleGrantedAuthority role =
                new SimpleGrantedAuthority("ROLE_" + user.getRole());

        // Create authentication object
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        List.of(role)
                );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Store security context in session
        request.getSession(true)
                .setAttribute("SPRING_SECURITY_CONTEXT",
                        SecurityContextHolder.getContext());
    }



}
