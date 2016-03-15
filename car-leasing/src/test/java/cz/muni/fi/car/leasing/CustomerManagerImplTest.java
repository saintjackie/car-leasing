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

    private Customer customer;
    private CustomerManager customerManager;

    @Before
    public void setUp() throws Exception {
        customerManager = new CustomerManagerImpl();
        customer = new Customer();
        customer.setId(1L);
        customer.setFullName("Franta Brambora");
        customer.setAddress("V hospode 4");
        customer.setBirthDate(LocalDate.now());
        customer.setPhoneNumber("0123456789");
    }

    @Test
    public void testCreate() throws Exception {
        customerManager.create(customer);
        Customer customer2 = customerManager.findById(customer.getId());
        assertThat("Created object is not equal to template", customer2, is(equalTo(customer)));
        assertThat("Created object is the same instance", customer2, is(not(sameInstance(customer))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongBirthDate() {
        Customer customerWrongBirthDate = new Customer();
        customerWrongBirthDate.setBirthDate(LocalDate.of(1893, Month.JUNE, 6));
    }

    @Test
    public void testDelete() throws Exception {
        customerManager.create(customer);
        customerManager.delete(customer.getId());
        Customer nullCustomer = customerManager.findById(customer.getId());
        assertThat("Found customer that should have been deleted", null, is(nullCustomer));
    }

    @Test
    public void testUpdate() throws Exception {
        Customer customer2 = new Customer();
        customer2.setId(3L);
        customer2.setFullName("Corey Taylor");
        customer2.setAddress("Des Moines, Iowa");
        customer2.setBirthDate(LocalDate.of(1973, Month.DECEMBER, 8));
        customer2.setPhoneNumber("555-666");
        customerManager.create(customer2);

        customer2.setFullName("Bruce Dickinson");
        customer2.setAddress("Ed Force One");
        customer2.setBirthDate(LocalDate.of(1958, Month.AUGUST, 7));
        customer2.setPhoneNumber("666");
        customerManager.update(customer2);

        Customer updatedCustomer = customerManager.findById(3L);
        assertThat("Updated customer in store is not equal to sent customer values", customer2,
                is(equalTo(updatedCustomer)));

    }

    @Test
    public void testFindById() throws Exception {
        customerManager.create(customer);
        Customer retrievedCustomer = customerManager.findById(customer.getId());
        assertThat("Expected customer was not retrieved", customer, is(equalTo(retrievedCustomer)));
    }

    @Test
    public void testFindByName() throws Exception {
        Customer customer2 = new Customer();
        customer2.setId(4L);
        customer2.setFullName(customer.getFullName());
        customer2.setAddress("Some address");
        customer2.setBirthDate(LocalDate.of(1994, Month.AUGUST, 6));
        customer2.setPhoneNumber("1053");
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
    public void testFindAll() throws Exception {
        Customer customer2 = new Customer();
        customer2.setId(4L);
        customer2.setFullName(customer.getFullName());
        customer2.setAddress("Some address");
        customer2.setBirthDate(LocalDate.of(1994, Month.AUGUST, 6));
        customer2.setPhoneNumber("1053");
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

    private static Comparator<Customer> idComparator = new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }; 
}