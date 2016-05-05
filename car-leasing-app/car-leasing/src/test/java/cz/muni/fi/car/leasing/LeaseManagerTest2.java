package cz.muni.fi.car.leasing;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jan Budinsky
 */

public class LeaseManagerTest2 {

    private DataSource dataSource;
    private LeaseManager leaseManager;
    private long carId;
    private long customerId;

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
        try(Connection connection = dataSource.getConnection()){
            connection.prepareStatement("CREATE TABLE lease ("
                    + "id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "carId BIGINT NOT NULL,"
                    + "customerId BIGINT NOT NULL,"
                    + "startTime TIMESTAMP,"
                    + "expectedEndTime TIMESTAMP,"
                    + "realEndTime TIMESTAMP,"
                    + "price DECIMAL,"
                    + "fee DECIMAL)").executeUpdate();
            connection.prepareStatement("CREATE TABLE customer (" +
                    "    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "    full_name VARCHAR(60) NOT NULL," +
                    "    phone VARCHAR(20)," +
                    "    birth_date DATE," +
                    "    address VARCHAR(100))").executeUpdate();
            connection.prepareStatement("CREATE TABLE CAR ("
                    + "id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "type VARCHAR(50),"
                    + "vendor VARCHAR(50),"
                    + "modelYear INT,"
                    + "seats INT,"
                    + "registrationPlate VARCHAR(10))").executeUpdate();
            insertCustomerAndCar(connection);
        }
        leaseManager = new LeaseManagerImpl(dataSource);
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:lease-manager-test");
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

    private void insertCustomerAndCar(Connection connection) throws SQLException {
        Customer customer = new Customer();
        customer.setFullName("Franta Brambora");
        PreparedStatement stCustomer = connection.prepareStatement("INSERT INTO customer (full_name) VALUES " +
                "('Franta Brambora')", Statement.RETURN_GENERATED_KEYS);
        int addedCustomers = stCustomer.executeUpdate();
        if (addedCustomers != 1) {
            throw new DBException("Internal Error: More rows (" + addedCustomers +
                    ") inserted when trying to insert customer");
        }
        ResultSet rsKeysCustomer = stCustomer.getGeneratedKeys();
        this.customerId = getIdFromResultSetKeys(rsKeysCustomer, customer);

        Car car = new Car();
        car.setType("Audi");
        car.setModelYear(1999);
        PreparedStatement stCar = connection.prepareStatement("INSERT INTO car (type, modelYear) VALUES ('Audi', " +
                        "1999)",
                Statement.RETURN_GENERATED_KEYS);
        int addedCars = stCar.executeUpdate();
        if (addedCars != 1) {
            throw new DBException("Internal Error: More rows (" + addedCars +
                    ") inserted when trying to insert car");
        }
        ResultSet rsKeysCar = stCar.getGeneratedKeys();
        this.carId = getIdFromResultSetKeys(rsKeysCar, car);
    }

    private Long getIdFromResultSetKeys(ResultSet keyRS, Object object) throws DBException, SQLException {
        if (keyRS.next()) {

            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new DBException("Internal Error: Generated key retrieving failed when trying to insert " +
                        "following object: " + object + " - wrong key fields count: " +
                        keyRS.getMetaData().getColumnCount());
            }

            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new DBException("Internal Error: Generated key retrieving failed when trying to insert " +
                        "following object: " + object + " - more keys found");
            }
            return result;
        } else {
            throw new DBException("Internal Error: Generated key retrieving failed when trying to insert following " +
                    "object: " + object + " - no key found");
        }
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE lease").executeUpdate();
            connection.prepareStatement("DROP TABLE customer").executeUpdate();
            connection.prepareStatement("DROP TABLE car").executeUpdate();
        }
    }

    private Lease newLeaseInstance(long carId, long customerId, LocalDateTime startTime, LocalDateTime expectedEndTime,
                                   LocalDateTime realEndTime, BigDecimal price, BigDecimal fee) {
        Lease lease = new Lease();
        lease.setCarId(carId);
        lease.setCustomerId(customerId);
        lease.setStartTime(startTime);
        lease.setExpectedEndTime(expectedEndTime);
        lease.setRealEndTime(realEndTime);
        lease.setPrice(price);
        lease.setFee(fee);
        return lease;
    }

    private static Comparator<Lease> idComparator = new Comparator<Lease>() {
        @Override
        public int compare(Lease o1, Lease o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

    @Test
    public void testCreate() {
        Lease lease = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,2,11,11), new BigDecimal("2000"), BigDecimal.ZERO);
        leaseManager.create(lease);
        Lease lease2 = leaseManager.findById(lease.getId());

        assertThat("Failed to retrieve created lease", null, is(not(lease2)));
        assertThat("Retrieved lease should be equals to inserted one", lease, equalTo(lease2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNull(){
        leaseManager.create(null);
    }

    @Test
    public void testDelete(){
        Lease lease = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,2,11,11), new BigDecimal("2000"), BigDecimal.ZERO);
        leaseManager.create(lease);
        leaseManager.delete(lease.getId());
        Lease lease2 = leaseManager.findById(lease.getId());

        assertThat("Deleted lease should not be in database", null, is(lease2));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteWrongId(){
        leaseManager.delete(-3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNull(){
        leaseManager.delete(null);
    }

    @Test
    public void testUpdate(){
        Lease lease = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,2,11,11), new BigDecimal("2000"), BigDecimal.ZERO);
        leaseManager.create(lease);
        lease.setPrice(BigDecimal.TEN);
        lease.setFee(BigDecimal.ONE);
        leaseManager.update(lease);
        Lease lease2 = leaseManager.findById(lease.getId());

        assertThat("Cannot find updated entity", null, is(not(lease2)));
        assertThat("Retrieved entity does not have correctly updated values", lease, equalTo(lease2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNull(){
        leaseManager.update(null);
    }

    @Test
    public void testFindByCustomer(){
        Lease lease = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,2,11,11), new BigDecimal("2000"), BigDecimal.ZERO);
        Lease lease2 = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,4,11,11), new BigDecimal("1900"), BigDecimal.TEN);
        leaseManager.create(lease);
        leaseManager.create(lease2);

        List<Lease> retrievedLeaseList = leaseManager.findByCustomer(lease.getCustomerId());
        List<Lease> expectedLeaseList = new ArrayList<>();
        expectedLeaseList.add(lease);
        expectedLeaseList.add(lease2);

        Collections.sort(retrievedLeaseList, idComparator);
        Collections.sort(expectedLeaseList, idComparator);

        assertThat("Expected leases were not retrieved", expectedLeaseList, is(equalTo(retrievedLeaseList)));
    }

    @Test
    public void testFindByCar(){
        Lease lease = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,2,11,11), new BigDecimal("2000"), BigDecimal.ZERO);
        Lease lease2 = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,4,11,11), new BigDecimal("1900"), BigDecimal.TEN);
        leaseManager.create(lease);
        leaseManager.create(lease2);

        List<Lease> retrievedLeaseList = leaseManager.findByCar(lease.getCarId());
        List<Lease> expectedLeaseList = new ArrayList<>();
        expectedLeaseList.add(lease);
        expectedLeaseList.add(lease2);

        Collections.sort(retrievedLeaseList, idComparator);
        Collections.sort(expectedLeaseList, idComparator);

        assertThat("Expected leases were not retrieved", expectedLeaseList, is(equalTo(retrievedLeaseList)));
    }

    @Test
    public void testFindAll(){
        Lease lease = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,2,11,11), new BigDecimal("2000"), BigDecimal.ZERO);
        Lease lease2 = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,4,11,11), new BigDecimal("1900"), BigDecimal.TEN);
        Lease lease3 = newLeaseInstance(this.carId, this.customerId, LocalDateTime.of(2015,1,1,11,11), LocalDateTime.of(
                2015,1,3,11,11), LocalDateTime.of(2015,1,2,1,2), new BigDecimal("2000"), BigDecimal.ZERO);
        leaseManager.create(lease);
        leaseManager.create(lease2);
        leaseManager.create(lease3);

        List<Lease> retrievedLeaseList = leaseManager.findAll();
        List<Lease> expectedLeaseList = new ArrayList<>();
        expectedLeaseList.add(lease);
        expectedLeaseList.add(lease2);
        expectedLeaseList.add(lease3);

        Collections.sort(retrievedLeaseList, idComparator);
        Collections.sort(expectedLeaseList, idComparator);

        assertThat("Expected leases were not retrieved", expectedLeaseList, equalTo(retrievedLeaseList));
    }

}
