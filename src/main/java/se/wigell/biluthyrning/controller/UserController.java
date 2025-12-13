package se.wigell.biluthyrning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.wigell.biluthyrning.model.Booking;
import se.wigell.biluthyrning.model.Car;
import se.wigell.biluthyrning.service.BookingService;
import se.wigell.biluthyrning.service.CarService;
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
    private final CarService carService;
    public UserController(UserService userService, BookingService bookingService, CarService carService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.carService = carService;
    }

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {

        if (request == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.validateLogin(
                request.getUsername(),
                request.getPassword()
        );

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(user);
    }


    @GetMapping("/me")
    public User getMyProfile(Authentication auth) {
        return userService.getByUsername(auth.getName());
    }

    @GetMapping("/my-bookings")
    public List<Booking> getMyBookings(Authentication auth) {
        return bookingService.getBookingsForUser(auth.getName());
    }


    @GetMapping("/cars")
    public List<Car> getCars() {
        return carService.getAllCars();
    }

    @GetMapping("/bookings")
    public List<Booking> getBookings() {
        return bookingService.getAllBookings();
    }

}

