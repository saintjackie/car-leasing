package cz.muni.fi.car.leasing;

import java.util.Objects;

/**
 * @author Jakub Holy
 */
public class Car {
    
    private Long id;
    private String type;
    private String vendor;
    private Integer modelYear;
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

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
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
        hash = 53 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
//    @Override
//    public String toString() {
//        return "Car{"+
//                "id=" + id +
//                ", type='" + type + '\'' +
//                ", vendor='" + vendor + '\'' +
//                ", modelYear='" + modelYear + '\'' +
//                ", seats='" + seats + '\'' +
//                ", registrationPlate='" + registrationPlate + '\'' +
//                "}";
//    }
    @Override
    public String toString(){
        return type + ", " + vendor +", "+modelYear+
                ", "+seats+", "+registrationPlate;
    }

    
    
    
}
