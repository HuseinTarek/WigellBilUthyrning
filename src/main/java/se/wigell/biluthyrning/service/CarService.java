package se.wigell.biluthyrning.service;

import org.springframework.stereotype.Service;
import se.wigell.biluthyrning.model.Car;
import se.wigell.biluthyrning.repository.CarRepository;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Integer id) {
        return carRepository.findById(id).orElse(null);
    }
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Integer id) {
        carRepository.deleteById(id);
    }
}
