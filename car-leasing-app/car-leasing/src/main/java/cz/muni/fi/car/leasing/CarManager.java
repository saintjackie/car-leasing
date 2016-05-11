package cz.muni.fi.car.leasing;

import java.util.List;

/**
 * @author Jakub Holy
 */
public interface CarManager {
    
    void create(Car car);
    void delete(Long carId);
    void update(Car car);
    Car findById(Long carId);
    List<Car> findByType(String type);
    List<Car> findAll();
    List<Car> findBySeats(Integer seats);
    List<Car> findByVendor(String vendor);
    List<Car> findByModelYear(Integer year);
    Car findByRegistration(String registrationPlate);
    
}
