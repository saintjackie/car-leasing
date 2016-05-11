package cz.muni.fi.car.leasing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * @author Jan Budinsky
 */

public class LeaseManagerImpl implements LeaseManager {

    final static Logger log = LoggerFactory.getLogger(LeaseManagerImpl.class);

    private final DataSource dataSource;
    private CarManager carManager;
    private CustomerManager customerManager;

    public LeaseManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.carManager = new CarManagerImpl(dataSource);
        this.customerManager = new CustomerManagerImpl(dataSource);
    }

    @Override
    public void create(Lease lease) {
        validate(lease);
        if (lease.getId() != null) {
            throw new IllegalArgumentException("Lease ID is set while it should have been generated by DB");
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "INSERT INTO lease (carId, customerId, startTime, expectedEndTime, realEndTime, price, fee) " +
                             "VALUES (?,?,?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            st.setLong(1, lease.getCarId());
            st.setLong(2, lease.getCustomerId());
            st.setTimestamp(3, Timestamp.valueOf(lease.getStartTime()));
            st.setTimestamp(4, Timestamp.valueOf(lease.getExpectedEndTime()));
            if(lease.getRealEndTime() != null){
                st.setTimestamp(5, Timestamp.valueOf(lease.getRealEndTime()));
            }else {
                st.setTimestamp(5,null);
            }
            st.setBigDecimal(6, lease.getPrice());
            if(lease.getFee() != null){
                st.setBigDecimal(7, lease.getFee());
            }else{
                st.setBigDecimal(7,null);
            }
            

            int addedRows = st.executeUpdate();

            if (addedRows != 1) {
                log.error("Error while creating Lease {} - more rows affected", lease);
                throw new DBException("Internal Error: More rows (" + addedRows +
                        ") inserted when trying to insert following lease: " + lease);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            lease.setId(getIdFromResultSetKeys(keyRS, lease));

        } catch (SQLException ex) {
            log.error("Error while creating Lease {}", lease);
            throw new DBException("Error when inserting following lease: " + lease, ex);
        }
    }

    @Override
    public void delete(Long id) throws DBException, IllegalArgumentException {
        if (id == null) {
            log.error("Error while deleting lease - null id given");
            throw new IllegalArgumentException("Cannot delete customer with null ID given");
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "DELETE FROM lease WHERE id = ?",
                     Statement.RETURN_GENERATED_KEYS)) {

            st.setLong(1, id);

            int deletedRows = st.executeUpdate();

            if (deletedRows == 0) {
                log.error("Error while deleting lease with id {} - not found", id);
                throw new EntityNotFoundException("Failed to find / delete customer with ID: " + id);
            }
            if (deletedRows != 1) {
                log.error("Error while deleting lease with id {} - more rows affected", id);
                throw new DBException("Multiple rows were affected while only 1 should have been deleted with ID: " +
                        id);
            }
        } catch (SQLException ex) {
            log.error("Error while deleting lease with id {}", id);
            throw new DBException("Error when deleting customer with following ID: " + id, ex);
        }
    }

    @Override
    public void update(Lease lease) {
        validate(lease);
        if (lease.getId() == null) {
            log.error("Error while updating lease - null id given");
            throw new IllegalArgumentException("Cannot update object with null ID");
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "UPDATE lease SET carId = ?, customerId = ?, startTime = ?, expectedEndTime = ?, " +
                             "realEndTime = ?, price = ?, fee = ? WHERE id = ?",
                     Statement.RETURN_GENERATED_KEYS)) {

            st.setLong(1, lease.getCarId());
            st.setLong(2, lease.getCustomerId());
            st.setTimestamp(3, Timestamp.valueOf(lease.getStartTime()));
            st.setTimestamp(4, Timestamp.valueOf(lease.getExpectedEndTime()));
            if(lease.getRealEndTime() != null){
                st.setTimestamp(5, Timestamp.valueOf(lease.getRealEndTime()));
            }else{
                st.setTimestamp(5,null);
            }
            st.setBigDecimal(6, lease.getPrice());
            if(lease.getFee() != null){
                st.setBigDecimal(7, lease.getFee());
            }else{
                st.setBigDecimal(7,null);
            }
            st.setLong(8, lease.getId());

            int updatedRows = st.executeUpdate();

            if (updatedRows == 0) {
                log.error("Error while updating lease with id {} - not found", lease.getId());
                throw new EntityNotFoundException("Lease with ID " + lease.getId() + "was not found in database");
            } else if (updatedRows != 1) {
                log.error("Error while updating lease with id {} - more rows affected", lease.getId());
                throw new DBException("More than 1 row updated: " + updatedRows);
            }

        } catch (SQLException ex) {
            log.error("Error while updating lease with id {}", lease.getId(), ex);
            throw new DBException("Error when updating following lease: " + lease, ex);
        }
    }

    @Override
    public Lease findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE id = ?")) {

            st.setLong(1, id);

            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                log.warn("No lease with id {} found in database", id);
                return null;
            }
            Lease lease = fillLeaseFromResultSet(rs);

            if (rs.next()) {
                log.error("Error in database - multiple leases with id {}", lease.getId());
                throw new DBException("Internal error: More leases with the same id (" + id + ") found." +
                        "First 2 leases: 1:\n" + lease + "2:\n" + fillLeaseFromResultSet(rs));
            }

            return lease;

        } catch (SQLException ex) {
            log.error("Error while retrieving lease with id {}", id, ex);
            throw new DBException("Error when retrieving lease with following id: " + id, ex);
        }
    }

    @Override
    public List<Lease> findByCustomer(Long customerId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE customerId = ?")) {

            st.setLong(1, customerId);

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved leases with customerId {}", customerId);
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving leases with customerId {}", customerId, ex);
            throw new DBException("Error when retrieving leases with customerId: " + customerId, ex);
        }
    }

    @Override
    public List<Lease> findByCar(Long carId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE carId = ?")) {

            st.setLong(1, carId);

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved leases with carId {}", carId);
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving leases with carId {}", carId, ex);
            throw new DBException("Error when retrieving leases with carId: " + carId, ex);
        }
    }

    @Override
    public List<Lease> findByStartTime(LocalDateTime startTime) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE startTime = ?")) {

            st.setTimestamp(1, Timestamp.valueOf(startTime));

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved leases with startTime {}", startTime);
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving leases with startTime {}", startTime, ex);
            throw new DBException("Error when retrieving leases with StartTime: " + startTime, ex);
        }
    }

    @Override
    public List<Lease> findByExpectedEndTime(LocalDateTime expectedEndTime) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE expectedEndTime = ?")) {

            st.setTimestamp(1, Timestamp.valueOf(expectedEndTime));

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved leases with expectedEndTime {}", expectedEndTime);
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving leases with expectedEndTime {}", expectedEndTime, ex);
            throw new DBException("Error when retrieving leases with ExpectedEndTime: " + expectedEndTime, ex);
        }
    }

    @Override
    public List<Lease> findByRealEndTime(LocalDateTime realEndTime) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE realEndTime = ?")) {

            st.setTimestamp(1, Timestamp.valueOf(realEndTime));

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved leases with realEndTime {}", realEndTime);
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving leases with realEndTime {}", realEndTime, ex);
            throw new DBException("Error when retrieving leases with realEndTime: " + realEndTime, ex);
        }
    }

    @Override
    public List<Lease> findByPrice(BigDecimal price) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE price = ?")) {

            st.setBigDecimal(1, price);

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved leases with price {}", price);
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving leases with price {}", price, ex);
            throw new DBException("Error when retrieving leases with price: " + price, ex);
        }
    }

    @Override
    public List<Lease> findByFee(BigDecimal fee) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM lease WHERE fee = ?")) {

            st.setBigDecimal(1, fee);

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved leases with fee {}", fee);
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving leases with fee {}", fee, ex);
            throw new DBException("Error when retrieving leases with fee: " + fee, ex);
        }
    }

    @Override
    public List<Lease> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT id, carId, customerId, startTime, expectedEndTime, realEndTime, price, fee FROM lease")) {

            List<Lease> leases = executeFindQuery(st);
            log.info("Retrieved all leases");
            return leases;

        } catch (SQLException ex) {
            log.error("Error while retrieving all leases", ex);
            throw new DBException("Error when retrieving list of all leases", ex);
        }
    }

    private List<Lease> executeFindQuery(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();

        List<Lease> retrievedLeases = new ArrayList<>();

        while (rs.next()) {
            Lease lease = fillLeaseFromResultSet(rs);
            retrievedLeases.add(lease);
        }
        return retrievedLeases;
    }

    private void validate(Lease lease) {
        if (lease == null) {
            throw new IllegalArgumentException("Cannot create / update lease from null object");
        }
        if (carManager.findById(lease.getCarId()) == null) {
            throw new IllegalArgumentException("Cannot find car with given ID");
        }
        if (customerManager.findById(lease.getCustomerId()) == null) {
            throw new IllegalArgumentException("Cannot find customer with given ID");
        }
    }

    private Long getIdFromResultSetKeys(ResultSet keyRS, Lease lease) throws DBException, SQLException {
        if (keyRS.next()) {

            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new DBException("Internal Error: Generated key retrieving failed when trying to insert " +
                        "following lease: " + lease + " - wrong key fields count: " +
                        keyRS.getMetaData().getColumnCount());
            }

            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new DBException("Internal Error: Generated key retrieving failed when trying to insert " +
                        "following lease: " + lease + " - more keys found");
            }
            return result;
        } else {
            throw new DBException("Internal Error: Generated key retrieving failed when trying to insert following " +
                    "lease: " + lease + " - no key found");
        }
    }

    private Lease fillLeaseFromResultSet(ResultSet rs) throws SQLException {
        Lease lease = new Lease();

        lease.setId(rs.getLong("id"));
        lease.setCarId(rs.getLong("carId"));
        lease.setCustomerId(rs.getLong("customerId"));
        lease.setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
        lease.setExpectedEndTime(rs.getTimestamp("expectedEndTime").toLocalDateTime());
        if(rs.getTimestamp("realEndTime") != null)
            lease.setRealEndTime(rs.getTimestamp("realEndTime").toLocalDateTime());
        lease.setPrice(rs.getBigDecimal("price"));
        if(rs.getBigDecimal("fee")!=null)
            lease.setFee(rs.getBigDecimal("fee"));

        return lease;
    }
}
