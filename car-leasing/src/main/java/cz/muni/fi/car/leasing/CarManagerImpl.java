package cz.muni.fi.car.leasing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

    CarManagerImpl() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void create(Car car) {
        
        if(car == null){
            throw new NullPointerException("car cannot be null");
        }
        
        try(    Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO CAR (type,vendor,modelYear,seats,registrationPlate) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)){
            
            st.setString(1,car.getType());
            st.setString(2, car.getVendor());
            st.setTimestamp(3, Timestamp.valueOf(car.getModelYear().atStartOfDay()));
            st.setInt(4, car.getSeats());
            st.setString(5, car.getRegistrationPlate());
            
            int addedRow = st.executeUpdate();
            if(addedRow != 1){
                throw new SQLException("more rows inserted instead of 1 row!");
            }
            
            ResultSet keyRS = st.getGeneratedKeys();
            car.setId(getKey(keyRS));
            
            
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
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM CAR WHERE id = ?")) {
            
            st.setLong(1, carId);
            
            int count = st.executeUpdate();
            if(count == 0){
                throw new SQLException("entity not found");
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
        
        try(    Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "UPDATE CAR SET type = ?, vendor = ?, modelYear = ?, seats = ?, registrationPlate = ? WHERE id = ?",
                        Statement.RETURN_GENERATED_KEYS)){
            
            st.setString(1,car.getType());
            st.setString(2, car.getVendor());
            st.setTimestamp(3, Timestamp.valueOf(car.getModelYear().atStartOfDay()));
            st.setInt(4, car.getSeats());
            st.setString(5, car.getRegistrationPlate());
            st.setLong(6, car.getId());
            
            int count = st.executeUpdate();
            if(count != 1){
                throw new SQLException("more rows inserted instead of 1 row!");
            }
                        
        } catch (SQLException ex) {

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Car> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Car> findBySeats(Integer seats) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Car> findByVendor(String vendor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Car findByRegistration(String registrationPlate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Car resultSetToCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setType(rs.getString("type"));
        car.setVendor(rs.getString("vendor"));
        car.setModelYear(rs.getTimestamp("modelYear").toLocalDateTime().toLocalDate());
        car.setSeats(rs.getInt("seats"));
        car.setRegistrationPlate(rs.getString("registrationPlate"));
        return car;
    }
    
}
