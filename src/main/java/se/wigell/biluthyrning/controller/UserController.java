package se.wigell.biluthyrning.controller;

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

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest credentials) {
        System.out.println("USERNAME = " + credentials.getUsername());
        System.out.println("PASSWORD = " + credentials.getPassword());
        return userService.validateLogin(credentials.getUsername(), credentials.getPassword());
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

