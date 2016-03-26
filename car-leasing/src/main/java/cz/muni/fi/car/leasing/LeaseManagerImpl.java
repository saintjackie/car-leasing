package cz.muni.fi.car.leasing;

import java.util.List;
import javax.sql.DataSource;

/**
 * @author Jan Budinsky
 */

public class LeaseManagerImpl implements LeaseManager {
    
    private final DataSource dataSource;
        
    public LeaseManagerImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }
    
    @Override
    public void create(Lease lease) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Lease lease) {

    }

    @Override
    public Lease findById(Long id) {
        return null;
    }

    @Override
    public List<Lease> findByCustomer(Long customerId) {
        return null;
    }

    @Override
    public List<Lease> findByCar(Long carId) {
        return null;
    }

    @Override
    public List<Lease> findAll() {
        return null;
    }
}
