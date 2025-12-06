package se.wigell.biluthyrning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.wigell.biluthyrning.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserUsername(String name);
}
