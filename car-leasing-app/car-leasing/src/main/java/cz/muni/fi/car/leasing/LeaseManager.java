package cz.muni.fi.car.leasing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jan Budinsky
 */

public interface LeaseManager {

    void create(Lease lease);
    void delete(Long id);
    void update(Lease lease);
    Lease findById(Long id);
    List<Lease> findByCustomer(Long customerId);
    List<Lease> findByCar(Long carId);
    List<Lease> findByStartTime(LocalDateTime startTime);
    List<Lease> findByExpectedEndTime(LocalDateTime expectedEndTime);
    List<Lease> findByRealEndTime(LocalDateTime realEndTime);
    List<Lease> findByPrice(BigDecimal price);
    List<Lease> findByFee(BigDecimal fee);
    List<Lease> findAll();

}
