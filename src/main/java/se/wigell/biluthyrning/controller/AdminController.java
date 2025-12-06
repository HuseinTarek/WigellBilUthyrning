package se.wigell.biluthyrning.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.wigell.biluthyrning.model.Booking;
import se.wigell.biluthyrning.model.Car;
import se.wigell.biluthyrning.service.BookingService;
import se.wigell.biluthyrning.service.CarService;
import se.wigell.biluthyrning.service.UserService;
import se.wigell.biluthyrning.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
private final UserService userService;
private final CarService carService;
private final BookingService bookingService;


    public AdminController(UserService userService, CarService carService, BookingService bookingService) {
        this.userService = userService;
        this.carService = carService;
        this.bookingService = bookingService;
    }

    @GetMapping("/users")
    public List<User> getUsers (){
        return userService.getAllUsers();
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
