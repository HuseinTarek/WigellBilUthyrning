package se.wigell.biluthyrning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final BookingService bookingService;
    public UserController(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

//    @PostMapping("/login")
//    public User login(@RequestBody LoginRequest credentials) {
//        System.out.println("USERNAME = " + credentials.getUsername());
//        System.out.println("PASSWORD = " + credentials.getPassword());
//        return userService.validateLogin(credentials.getUsername(), credentials.getPassword());
//    }

//    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
//        log.info("Login attempt for username='{}'", loginRequest.getUsername());
//        User user = userService.validateLogin(loginRequest.getUsername(), loginRequest.getPassword());
//        if (user != null) {
//            log.info("Login successful for username='{}'", loginRequest.getUsername());
//            return ResponseEntity.ok(user);
//        } else {
//            log.warn("Login failed for username='{}'", loginRequest.getUsername());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }



    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest != null ? loginRequest.getUsername() : null;
            log.info("Login attempt for username='{}'", username);
            User user = userService.validateLogin(username, loginRequest != null ? loginRequest.getPassword() : null);
            if (user != null) {
                log.info("Login successful for username='{}'", username);
                return ResponseEntity.ok(user);
            } else {
                log.warn("Login failed for username='{}'", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception ex) {
            log.error("Unexpected error during login", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

