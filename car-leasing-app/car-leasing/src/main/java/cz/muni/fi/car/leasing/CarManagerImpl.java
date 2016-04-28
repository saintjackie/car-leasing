package cz.muni.fi.car.leasing;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * @author Jakub Holy
 */
public class CarManagerImpl implements CarManager{

    private DataSource dataSource;
    
    public CarManagerImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public CarManagerImpl() {
    }
    
    
    @Override
    public void create(Car car) {
        
        if(car == null){
            throw new NullPointerException("car cannot be null");
        }
        
        try(Connection connection = dataSource.getConnection()){
            
            connection.setAutoCommit(false);
            try(PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO CAR (type,vendor,modelYear,seats,registrationPlate) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)){
                
            st.setString(1,car.getType());
            st.setString(2, car.getVendor());
            st.setDate(3, Date.valueOf(car.getModelYear()));
            st.setInt(4, car.getSeats());
            st.setString(5, car.getRegistrationPlate());
            
            int addedRow = st.executeUpdate();
            connection.commit();
            if(addedRow != 1){
                throw new SQLException("more rows inserted instead of 1 row!");
            }
            
            ResultSet keyRS = st.getGeneratedKeys();
            car.setId(getKey(keyRS));

            }catch(SQLException ex){
                connection.rollback();
            }finally{
                
                connection.setAutoCommit(true);
            }
                                    
        } catch (SQLException ex) {
            Logger.getLogger(CarManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    private Long getKey(ResultSet keyRS) throws SQLException{
        if(keyRS.next()){
            Long result = keyRS.getLong(1);
            return result;
        }else{
            throw new SQLException("no key found.");
        }
        
    }

    @Override
    public void delete(Long carId) {
        if(carId == null){
            throw new NullPointerException("id cannot be null.");
        }
        if(carId < 0){
            throw new IllegalArgumentException("id cannot be negative");
        }
        
        try (Connection connection = dataSource.getConnection()) {
            
            connection.setAutoCommit(false);
            try(PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM CAR WHERE id = ?")){
                
            st.setLong(1, carId);
            
            int count = st.executeUpdate();
            connection.commit();
            if(count == 0){
                throw new SQLException("entity not found");
            }
            
            }catch(SQLException ex){
                connection.rollback();
            }finally{
                connection.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CarManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @Override
    public void update(Car car){
         if(car == null){
            throw new NullPointerException("car cannot be null");
        }
        
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            
            try(PreparedStatement st = connection.prepareStatement(
                        "UPDATE CAR SET type = ?, vendor = ?, modelYear = ?, seats = ?, registrationPlate = ? WHERE id = ?")){
                
                st.setString(1, car.getType());
                st.setString(2, car.getVendor());
                st.setDate(3, Date.valueOf(car.getModelYear()));
                st.setInt(4, car.getSeats());
                st.setString(5, car.getRegistrationPlate());
                st.setLong(6, car.getId());

                int count = st.executeUpdate();
                connection.commit();
                if (count != 1) {
                    throw new SQLException("more rows inserted instead of 1 row!");
                }

            }catch(SQLException ex){
                connection.rollback();
            }finally{
                connection.setAutoCommit(true);
            }
                          
        } catch (SQLException ex) {
            Logger.getLogger(CarManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Car findById(Long carId){
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,type,vendor,modelYear,seats,registrationPlate FROM CAR WHERE id = ?")) {
            
            st.setLong(1, carId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Car car = resultSetToCar(rs);
                return car;
            }else{
                return null;
            }
            
        
        }   catch (SQLException ex) {
            return null;
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
            while(rs.next()){
                cars.add(resultSetToCar(rs));
            }
            return cars;
            
        } catch (SQLException ex) {
            return null;
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
            while(rs.next()){
                cars.add(resultSetToCar(rs));
            }
            return cars;
            
        } catch (SQLException ex) {
            return null;
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
            while(rs.next()){
                cars.add(resultSetToCar(rs));
            }
            return cars;
            
        } catch (SQLException ex) {
            return null;
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
            while(rs.next()){
                cars.add(resultSetToCar(rs));
            }
            return cars;
            
        } catch (SQLException ex) {
            return null;
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
                Car car = resultSetToCar(rs);
                return car;
            }else{
                return null;
            }
            
        } catch (SQLException ex) {
            return null;
        }
    }

    private Car resultSetToCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setType(rs.getString("type"));
        car.setVendor(rs.getString("vendor"));
        car.setModelYear(rs.getDate("modelYear").toLocalDate());
        car.setSeats(rs.getInt("seats"));
        car.setRegistrationPlate(rs.getString("registrationPlate"));
        return car;
    }
    
}
