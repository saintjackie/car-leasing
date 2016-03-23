package cz.muni.fi.car.leasing;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 * @author Jan Budinsky
 */

public class CustomerManagerImplTest {

    private CustomerManager customerManager;

    @Before
    public void setUp() throws Exception {
        customerManager = new CustomerManagerImpl();
    }

    @Test
    public void testCreate() throws Exception {
        Customer customer = newCustomerInstance(1L, "Franta Brambora", "V hospode 4", LocalDate.now(), "0123456789");
        customerManager.create(customer);
        Customer customer2 = customerManager.findById(customer.getId());
        assertThat("Created object is not equal to template", customer2, is(equalTo(customer)));
        assertThat("Created object is the same instance", customer2, is(not(sameInstance(customer))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongBirthDate() {
        Customer customer = newCustomerInstance(1L, "Franta Brambora", "V hospode 4", LocalDate.of(1893, Month.JUNE, 6),
                "0123456789");
        customerManager.create(customer);
    }

    @Test
    public void testDelete() throws Exception {
        Customer customer = newCustomerInstance(1L, "Franta Brambora", "V hospode 4", LocalDate.now(), "0123456789");
        customerManager.create(customer);
        customerManager.delete(customer.getId());
        Customer nullCustomer = customerManager.findById(customer.getId());
        assertThat("Found customer that should have been deleted", null, is(nullCustomer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNotExisting() {
        customerManager.delete(-123L);
    }

    @Test
    public void testUpdate() throws Exception {
        Customer customer = newCustomerInstance(3L, "Corey Taylor", "Des Moines, Iowa",
                LocalDate.of(1973, Month.DECEMBER, 8), "555-666");
        customerManager.create(customer);

        //Customer customer2 = newCustomerInstance(customer.getId(), "Bruce Dickinson", "Ed Force One",
        //        LocalDate.of(1958, Month.AUGUST, 7), "666");
        //customerManager.update(customer2);

        customer.setFullName("Bruce Dickinson");
        customer.setAddress("Ed Force One");
        customer.setBirthDate(LocalDate.of(1958, Month.AUGUST, 7));
        customer.setPhoneNumber("666");
        customerManager.update(customer);

        Customer updatedCustomer = customerManager.findById(3L);
        assertThat("Updated customer in store is not equal to sent customer values", customer,
                is(equalTo(updatedCustomer)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNonExisting() {
        Customer customer = newCustomerInstance(-666L, "Franta Brambora", "V hospode 4", LocalDate.now(), "0123456789");
        customerManager.update(customer);
    }

    @Test
    public void testFindById() throws Exception {
        Customer customer = newCustomerInstance(1L, "Franta Brambora", "V hospode 4", LocalDate.now(), "0123456789");
        customerManager.create(customer);
        Customer retrievedCustomer = customerManager.findById(customer.getId());
        assertThat("Expected customer was not retrieved", customer, is(equalTo(retrievedCustomer)));
    }

    @Test()
    public void testFindByWrongId (){
        Customer customer = newCustomerInstance(1L, "Franta Brambora", "V hospode 4", LocalDate.now(), "0123456789");
        customerManager.create(customer);
        Customer retrievedCustomer = customerManager.findById(-123L);
        assertThat("Retrieved notnull object while null expected", null, is(retrievedCustomer));
    }

    @Test
    public void testFindByName() throws Exception {
        Customer customer = newCustomerInstance(1L, "Franta Brambora", "V hospode 4", LocalDate.now(), "0123456789");
        Customer customer2 = newCustomerInstance(4L, customer.getFullName(), "Some address",
                LocalDate.of(1994, Month.AUGUST, 6), "1053");
        customerManager.create(customer);
        customerManager.create(customer2);

        List<Customer> retrievedCustomerList = customerManager.findByName(customer.getFullName());
        List<Customer> expectedCustomerList = new ArrayList<>();
        expectedCustomerList.add(customer);
        expectedCustomerList.add(customer2);

        Collections.sort(retrievedCustomerList, idComparator);
        Collections.sort(expectedCustomerList, idComparator);

        assertThat("Expected customers was not retrieved", expectedCustomerList, is(equalTo(retrievedCustomerList)));
    }

    @Test
    public void testFindByWrongName() {
        List<Customer> customerList = customerManager.findByName("Definitely not a name");
        assertThat("Expected empty list", null, is(customerList));
    }

    @Test
    public void testFindAll() throws Exception {
        Customer customer = newCustomerInstance(1L, "Franta Brambora", "V hospode 4", LocalDate.now(), "0123456789");
        Customer customer2 = newCustomerInstance(4L, customer.getFullName(), "Some address",
                LocalDate.of(1994, Month.AUGUST, 6), "1053");
        customerManager.create(customer);
        customerManager.create(customer2);

        List<Customer> retrievedCustomerList = customerManager.findAll();
        List<Customer> expectedCustomerList = new ArrayList<>();
        expectedCustomerList.add(customer);
        expectedCustomerList.add(customer2);

        Collections.sort(retrievedCustomerList, idComparator);
        Collections.sort(expectedCustomerList, idComparator);

        assertThat("Expected customers was not retrieved", expectedCustomerList, is(equalTo(retrievedCustomerList)));
    }

    private Customer newCustomerInstance(Long id, String fullName, String address, LocalDate birthDate, String phoneNumber) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFullName(fullName);
        customer.setAddress(address);
        customer.setBirthDate(birthDate);
        customer.setPhoneNumber(phoneNumber);
        return customer;
    }

    private static Comparator<Customer> idComparator = new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };
}