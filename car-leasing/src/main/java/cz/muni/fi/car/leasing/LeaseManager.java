package cz.muni.fi.car.leasing;

import java.util.List;

/**
 * @author Jakub Holy
 */
public interface LeaseManager {
    
    public void create(Lease lease);
    public void delete(Long leaseId);
    public void update(Lease lease);
    public Lease findById(Long leaseId);
    public List<Lease> findByCustomer(Customer customer);
    public List<Lease> findByCar(Car car);
    public List<Lease> findAll();
    
}
