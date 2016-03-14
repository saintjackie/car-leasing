package cz.muni.fi.car.leasing;

import java.util.List;

/**
 * @author Jakub Holy
 */
public interface CustomerManager {
    
    public void create(Customer customer);
    public void delete(Long customerId);
    public void update(Customer customer);
    public Customer findById(Long customerId);
    public List<Customer> findByName(String name);
    public List<Customer> findAll();
            
}
