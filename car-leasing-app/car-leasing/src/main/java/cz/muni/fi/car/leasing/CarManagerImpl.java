package cz.muni.fi.car.leasing;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * @author Jakub Holy
 */
public class CarManagerImpl implements CarManager {

    final static Logger log = LoggerFactory.getLogger(CarManagerImpl.class);

    private DataSource dataSource;

    public CarManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CarManagerImpl() {
    }


    @Override
    public void create(Car car) {

        if (car == null) {
            log.error("Error while creating car - got null object instead of car");
            throw new NullPointerException("car cannot be null");
        }

        try (Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "INSERT INTO CAR (type,vendor,modelYear,seats,registrationPlate) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                st.setString(1, car.getType());
                st.setString(2, car.getVendor());
                st.setInt(3, car.getModelYear());
                st.setInt(4, car.getSeats());
                st.setString(5, car.getRegistrationPlate());

                int addedRow = st.executeUpdate();
                
                if (addedRow != 1) {
                    throw new DBException("more rows inserted instead of 1 row!");
                }

                ResultSet keyRS = st.getGeneratedKeys();
                car.setId(getKey(keyRS));

        } catch (SQLException ex) {
            log.error("Error while creating Car {}", car, ex);
            throw new DBException("Error when inserting following car: " + car, ex);
        }

    }

    private Long getKey(ResultSet keyRS) throws SQLException {
        if (keyRS.next()) {
            return keyRS.getLong(1);
        } else {
            throw new SQLException("no key found.");
        }

    }

    @Override
    public void delete(Long carId) {
        if (carId == null) {
            log.error("Error while deleting car - got null instead of id");
            throw new NullPointerException("id cannot be null.");
        }
        if (carId < 0) {
            log.error("Error while deleting car - id has to be positive, got {}", carId);
            throw new IllegalArgumentException("id cannot be negative");
        }

        try (Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM CAR WHERE id = ?")) {

                st.setLong(1, carId);

                int count = st.executeUpdate();

                if (count == 0) {
                    log.error("Error while deleting car with id {} - not found", carId);
                    throw new EntityNotFoundException("Failed to find / delete customer with ID: " + carId);
                }

        } catch (SQLException ex) {
            log.error("Error while deleting car with id {}", carId, ex);
            throw new DBException("Error when deleting car with following ID: " + carId, ex);
        }
    }

    @Override
    public void update(Car car) {
        if (car == null) {
            log.error("Error while updating car - got null object");
            throw new NullPointerException("car cannot be null");
        }

        try (Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "UPDATE CAR SET type = ?, vendor = ?, modelYear = ?, seats = ?, registrationPlate = ? WHERE id = ?")) {

                st.setString(1, car.getType());
                st.setString(2, car.getVendor());
                st.setInt(3, car.getModelYear());
                st.setInt(4, car.getSeats());
                st.setString(5, car.getRegistrationPlate());
                st.setLong(6, car.getId());

                int count = st.executeUpdate();

                if (count != 1) {
                    log.error("Error while updating car with id {} - more rows affected", car.getId());
                    throw new DBException("More than 1 row updated: " + count);
                }
        } catch (SQLException ex) {
            log.error("Error while updating Car {}", car);
            throw new DBException("Error when updating following car: " + car.getId(), ex);
        }
    }

    @Override
    public Car findById(Long carId) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR WHERE id = ?")) {

            st.setLong(1, carId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                log. info("Retrieved car with id {}", carId);
                return resultSetToCar(rs);
            } else {
                log.warn("Car with id {} not found in database", carId);
                return null;
            }

        } catch (SQLException ex) {
            log.error("Error while retrieving car with id {}", carId);
            throw new DBException("Error when retrieving car with following id: " + carId, ex);
        }
    }

    @Override
    public List<Car> findByType(String type) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR WHERE type = ?")) {

            st.setString(1, type);
            ResultSet rs = st.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(resultSetToCar(rs));
            }
            log.info("Retrieved cars with type {}", type);
            return cars;

        } catch (SQLException ex) {
            log.error("Error while retrieving car with type {}", type, ex);
            throw new DBException("Error when retrieving car with following type: " + type, ex);
        }
    }

    @Override
    public List<Car> findAll() {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR")) {

            ResultSet rs = st.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(resultSetToCar(rs));
            }
            log.info("Retrieved all cars");
            return cars;

        } catch (SQLException ex) {
            log.error("Error while retrieving all cars", ex);
            throw new DBException("Error when retrieving list of all cars", ex);
        }
    }

    @Override
    public List<Car> findBySeats(Integer seats) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR WHERE seats = ?")) {

            st.setInt(1, seats);
            ResultSet rs = st.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(resultSetToCar(rs));
            }
            log.info("Retrieved cars with seats number of {}", seats);
            return cars;

        } catch (SQLException ex) {
            log.error("Error while retrieving cars with seats number of {}", seats, ex);
            throw new DBException("Error while retrieving cars with seats number of:" + seats, ex);
        }
    }
    
    @Override
    public List<Car> findByModelYear(Integer year) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR WHERE modelYear = ?")) {

            st.setInt(1, year);
            ResultSet rs = st.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(resultSetToCar(rs));
            }
            log.info("Retrieved cars with modelYear number of {}", year);
            return cars;

        } catch (SQLException ex) {
            log.error("Error while retrieving cars with modelYear number of {}", year, ex);
            throw new DBException("Error while retrieving cars with modelYear number of:" + year, ex);
        }
    }

    @Override
    public List<Car> findByVendor(String vendor) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR WHERE vendor = ?")) {

            st.setString(1, vendor);
            ResultSet rs = st.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(resultSetToCar(rs));
            }
            log.info("Retrieved cars with vendor {}", vendor);
            return cars;

        } catch (SQLException ex) {
            log.error("Error while retrieving cars with vendor {}", vendor, ex);
            throw new DBException("Error while retrieving cars with vendor:" + vendor, ex);
        }
    }

    @Override
    public Car findByRegistration(String registrationPlate) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR WHERE registrationPlate = ?")) {

            st.setString(1, registrationPlate);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                log.info("Retrieved cars with registration plate {}", registrationPlate);
                return resultSetToCar(rs);
            } else {
                return null;
            }

        } catch (SQLException ex) {
            log.error("Error while retrieving cars with registration plate {}", registrationPlate, ex);
            throw new DBException("Error while retrieving cars with registration plate:" + registrationPlate, ex);
        }
    }

    private Car resultSetToCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setType(rs.getString("type"));
        car.setVendor(rs.getString("vendor"));
        car.setModelYear(rs.getInt("modelYear"));
        car.setSeats(rs.getInt("seats"));
        car.setRegistrationPlate(rs.getString("registrationPlate"));
        return car;
    }

}
