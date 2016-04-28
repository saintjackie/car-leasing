package cz.muni.fi.car.leasing;

import java.util.List;

/**
 * @author Jan Budinsky
 */

public interface CustomerManager {

    void create(Customer customer);
    void delete(Long id);
    void update(Customer customer);
    Customer findById(Long id);
    List<Customer> findByName(String name);
    List<Customer> findAll();

}
