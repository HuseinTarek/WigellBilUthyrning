package se.wigell.biluthyrning.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.wigell.biluthyrning.service.UserService;
import se.wigell.biluthyrning.model.User;

import java.util.ArrayList;
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

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        request.getSession(true)
                .setAttribute("SPRING_SECURITY_CONTEXT",
                        SecurityContextHolder.getContext());
    }


}
