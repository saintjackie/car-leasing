package cz.muni.fi.car.leasing;

import java.time.LocalDate;

/**
 * @author Jan Budinsky
 */
public class Customer {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private LocalDate birthDate;
    private String address;

    public Customer() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if(fullName == null) {
            throw new IllegalArgumentException("Every customer should have a proper name, not null");
        }
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        if(birthDate.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new IllegalArgumentException("Anyone born before year 1900 is probably not suitable to drive a car");
        }
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (fullName != null ? !fullName.equals(customer.fullName) : customer.fullName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(customer.phoneNumber) : customer.phoneNumber != null)
            return false;
        if (birthDate != null ? !birthDate.equals(customer.birthDate) : customer.birthDate != null) return false;
        return address != null ? address.equals(customer.address) : customer.address == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

//    @Override
//    public String toString() {
//        return "Customer{" +
//                "id=" + id +
//                ", fullName='" + fullName + '\'' +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                ", birthDate=" + birthDate +
//                ", address='" + address + '\'' +
//                '}';
//    }
    @Override
    public String toString(){
        return fullName+", "+phoneNumber+", "+birthDate+", "+address;
    }
}
