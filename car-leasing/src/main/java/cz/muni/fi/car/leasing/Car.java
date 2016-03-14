package cz.muni.fi.car.leasing;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Jakub Holy
 */
public class Car {
    
    private Long id;
    private String type;
    private String vendor;
    private LocalDate modelYear;
    private Integer seats;
    private String registrationPlate;
    
    public Car(){        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getModelYear() {
        return modelYear;
    }

    public void setModelYear(LocalDate modelYear) {
        this.modelYear = modelYear;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.type);
        hash = 97 * hash + Objects.hashCode(this.vendor);
        hash = 97 * hash + Objects.hashCode(this.modelYear);
        hash = 97 * hash + Objects.hashCode(this.seats);
        hash = 97 * hash + Objects.hashCode(this.registrationPlate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.vendor, other.vendor)) {
            return false;
        }
        if (!Objects.equals(this.modelYear, other.modelYear)) {
            return false;
        }
        if (!Objects.equals(this.seats, other.seats)) {
            return false;
        }
        if (!Objects.equals(this.registrationPlate, other.registrationPlate)) {
            return false;
        }
        return true;
    }
    
    
}
