package cz.muni.fi.car.leasing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * @author Jakub Holy
 */
public class LeaseManagerTest {

    private DataSource dataSource;
    private LeaseManager leaseManager;
    private Long customerId;
    private Long carId;

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
        try(Connection connection = dataSource.getConnection()){
            connection.prepareStatement("CREATE TABLE LEASE ("
                    + "id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "carId BIGINT,"
                    + "customerId BIGINT,"
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
    public void dropTable() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE LEASE").executeUpdate();
            connection.prepareStatement("DROP TABLE car").executeUpdate();
            connection.prepareStatement("DROP TABLE customer").executeUpdate();
        }
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:lease-manager-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    private Lease newLeaseInstance(){
        Lease lease = new Lease();
        lease.setCarId(this.carId);
        lease.setCustomerId(this.customerId);
        lease.setStartTime(LocalDateTime.of(2015,1,1,11,11));
        lease.setExpectedEndTime(LocalDateTime.of(2015,1,3,11,11));
        lease.setRealEndTime(LocalDateTime.of(2015, 1,2,11,11));
        lease.setPrice(new BigDecimal("2000"));
        lease.setFee(BigDecimal.ZERO);
        return lease;
    }

    private Lease newLeaseInstance2(){
        Lease lease = new Lease();
        lease.setCarId(this.carId);
        lease.setCustomerId(this.customerId);
        lease.setStartTime(LocalDateTime.of(2015,1,1,11,11));
        lease.setExpectedEndTime(LocalDateTime.of(2015,1,3,11,11));
        lease.setRealEndTime(LocalDateTime.of(2015, 1,4,11,11));
        lease.setPrice(new BigDecimal("1900"));
        lease.setFee(BigDecimal.TEN);
        return lease;
    }

    @Test
    public void testCreateLease() {
        Lease lease = newLeaseInstance();
        leaseManager.create(lease);
        Lease lease2 = leaseManager.findById(lease.getId());

        assertNotNull(lease2);
        assertEquals(lease, lease2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullLease(){
        leaseManager.create(null);
    }

    @Test
    public void testDeleteLease(){
        Lease lease = newLeaseInstance();
        leaseManager.create(lease);
        leaseManager.delete(lease.getId());
        Lease lease2 = leaseManager.findById(lease.getId());

        assertNull(lease2);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteIllegalArgumentException(){
        leaseManager.delete(-3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullPointerException(){
        leaseManager.delete(null);
    }

    @Test
    public void testUpdateLease(){
        Lease lease = newLeaseInstance();
        leaseManager.create(lease);
        lease.setPrice(BigDecimal.TEN);
        lease.setFee(BigDecimal.ONE);
        leaseManager.update(lease);
        Lease lease2 = leaseManager.findById(lease.getId());

        assertNotNull(lease2);
        assertEquals(lease,lease2);
        assertEquals(BigDecimal.TEN,lease2.getPrice());
        assertEquals(BigDecimal.ONE,lease2.getFee());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullLease(){
        leaseManager.update(null);
    }

    @Test
    public void testFindByCustomer(){
        Lease lease = newLeaseInstance();
        Lease lease2 = newLeaseInstance2();
        leaseManager.create(lease);
        leaseManager.create(lease2);

        List<Lease> leases = leaseManager.findByCustomer(lease.getCustomerId());

        assertNotNull(leases);
        assertTrue(leases.contains(lease));
        assertTrue(leases.contains(lease2));
        assertEquals(2,leases.size());
        assertNotNull(leases.get(0));
        assertNotNull(leases.get(1));
    }

    @Test
    public void testFindByCar(){
        Lease lease = newLeaseInstance();
        Lease lease2 = newLeaseInstance2();
        leaseManager.create(lease);
        leaseManager.create(lease2);

        List<Lease> leases = leaseManager.findByCar(lease.getCarId());

        assertNotNull(leases);
        assertTrue(leases.contains(lease));
        assertEquals(2,leases.size());
        assertNotNull(leases.get(0));
        assertNotNull(leases.get(1));
    }

    @Test
    public void testFindAllLeases(){
        Lease lease = newLeaseInstance();
        Lease lease2 = newLeaseInstance2();

        leaseManager.create(lease);
        leaseManager.create(lease2);


        List<Lease> leases = leaseManager.findAll();

        assertNotNull(leases);
        assertTrue(leases.contains(lease));
        assertTrue(leases.contains(lease2));

        assertEquals(2,leases.size());
        assertNotNull(leases.get(0));
        assertNotNull(leases.get(1));


    }


}
