package se.wigell.biluthyrning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.wigell.biluthyrning.model.Car;


public interface CarRepository extends JpaRepository<Car, Integer> {
}
