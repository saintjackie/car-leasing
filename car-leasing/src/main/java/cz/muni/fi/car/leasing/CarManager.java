package cz.muni.fi.car.leasing;

import java.util.List;

/**
 *
 * @author Jakub Holy
 */
public interface CarManager {
    public void create(Car car);
    public void delete(Long carId);
    public void update(Car car);
    public Car findById(Long carId);
    public List<Car> findByType(String type);
    public List<Car> findAll();
    public List<Car> findBySeats(Integer seats);
    public List<Car> findByVendor(String vendor);
    public Car findByRegistration(String registrationPlate);
    
}
