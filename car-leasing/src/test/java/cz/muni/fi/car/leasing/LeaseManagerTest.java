package cz.muni.fi.car.leasing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
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
        }
        leaseManager = new LeaseManagerImpl(dataSource);
    }

    @After
    public void dropTable() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE LEASE").executeUpdate();
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
        lease.setCarId(1L);
        lease.setCustomerId(1L);
        lease.setStartTime(LocalDateTime.of(2015,1,1,11,11));
        lease.setExpectedEndTime(LocalDateTime.of(2015,1,3,11,11));
        lease.setRealEndTime(LocalDateTime.of(2015, 1,2,11,11));
        lease.setPrice(new BigDecimal("2000"));
        lease.setFee(BigDecimal.ZERO);
        return lease;
    }

    private Lease newLeaseInstance2(){
        Lease lease = new Lease();
        lease.setCarId(2L);
        lease.setCustomerId(1L);
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

    @Test(expected = NullPointerException.class)
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

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteIllegalArgumentException(){
        leaseManager.delete(-3L);
    }

    @Test(expected = NullPointerException.class)
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

    @Test(expected = NullPointerException.class)
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
        assertEquals(1,leases.size());
        assertNotNull(leases.get(0));
    }

    @Test
    public void testFindAllLeases(){
        Lease lease = newLeaseInstance();
        Lease lease2 = newLeaseInstance2();
        Lease lease3 = newLeaseInstance();
        leaseManager.create(lease);
        leaseManager.create(lease2);
        lease3.setCustomerId(3L);
        leaseManager.create(lease);

        List<Lease> leases = leaseManager.findAll();

        assertNotNull(leases);
        assertTrue(leases.contains(lease));
        assertTrue(leases.contains(lease2));
        assertTrue(leases.contains(lease3));
        assertEquals(3,leases.size());
        assertNotNull(leases.get(0));
        assertNotNull(leases.get(1));
        assertNotNull(leases.get(2));

    }


}
