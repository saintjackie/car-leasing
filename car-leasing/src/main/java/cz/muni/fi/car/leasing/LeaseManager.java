package cz.muni.fi.car.leasing;

import java.util.List;

/**
 * @author Jan Budinsky
 */

public interface LeaseManager {

    void create(Lease lease);
    void delete(Long id);
    void update(Lease lease);
    Lease findById(Long id);
    List<Lease> findByCustomer(Customer customer);
    List<Lease> findByCar(Car car);
    List<Lease> findAll();

}
