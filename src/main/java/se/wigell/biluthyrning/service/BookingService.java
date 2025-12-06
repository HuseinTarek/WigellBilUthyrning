package se.wigell.biluthyrning.service;

import org.springframework.stereotype.Service;
import se.wigell.biluthyrning.model.Booking;
import se.wigell.biluthyrning.repository.BookingRepository;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Integer id){
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking saveBooking(Booking booking){
        return bookingRepository.save(booking);

    }
    public void deleteBooking(Integer id){
        bookingRepository.deleteById(id);
    }

    public List<Booking> getBookingsForUser(String name) {
        return bookingRepository.findByUserUsername(name);
    }
}
