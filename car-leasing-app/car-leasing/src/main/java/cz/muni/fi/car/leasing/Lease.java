package cz.muni.fi.car.leasing;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Jan Budinsky
 */
public class Lease {

    private Long id;
    private Long carId;
    private Long customerId;
    private LocalDateTime startTime;
    private LocalDateTime expectedEndTime;
    private LocalDateTime realEndTime;
    private BigDecimal price;
    private BigDecimal fee;

    public Lease() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
//        if(carId == null) {
//            throw new IllegalArgumentException("Cannot create lease with null carId");
//        }
        this.carId = carId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
//        if(customerId == null) {
//            throw new IllegalArgumentException("Cannot create lease with null customerId");
//        }
        this.customerId = customerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getExpectedEndTime() {
        return expectedEndTime;
    }

    public void setExpectedEndTime(LocalDateTime expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public LocalDateTime getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(LocalDateTime realEndTime) {
        this.realEndTime = realEndTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lease lease = (Lease) o;

        if (id != null ? !id.equals(lease.id) : lease.id != null) return false;
        if (carId != null ? !carId.equals(lease.carId) : lease.carId != null) return false;
        if (customerId != null ? !customerId.equals(lease.customerId) : lease.customerId != null) return false;
        if (startTime != null ? !startTime.equals(lease.startTime) : lease.startTime != null) return false;
        if (expectedEndTime != null ? !expectedEndTime.equals(lease.expectedEndTime) : lease.expectedEndTime != null)
            return false;
        if (realEndTime != null ? !realEndTime.equals(lease.realEndTime) : lease.realEndTime != null) return false;
        if (price != null ? !price.equals(lease.price) : lease.price != null) return false;
        return fee != null ? fee.equals(lease.fee) : lease.fee == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (carId != null ? carId.hashCode() : 0);
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (expectedEndTime != null ? expectedEndTime.hashCode() : 0);
        result = 31 * result + (realEndTime != null ? realEndTime.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (fee != null ? fee.hashCode() : 0);
        return result;
    }
}
