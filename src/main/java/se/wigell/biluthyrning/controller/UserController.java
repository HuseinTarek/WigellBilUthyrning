package se.wigell.biluthyrning.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.wigell.biluthyrning.model.Booking;
import se.wigell.biluthyrning.service.BookingService;
import se.wigell.biluthyrning.service.UserService;
import se.wigell.biluthyrning.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    public UserController(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.validateLogin(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public User getMyProfile(Authentication auth) {
        return userService.getByUsername(auth.getName());
    }

    @GetMapping("/my-bookings")
    public List<Booking> getMyBookings(Authentication auth) {
        return bookingService.getBookingsForUser(auth.getName());
    }

}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
