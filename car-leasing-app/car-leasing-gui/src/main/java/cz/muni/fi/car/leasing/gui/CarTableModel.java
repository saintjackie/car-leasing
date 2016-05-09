package cz.muni.fi.car.leasing.gui;


import cz.muni.fi.car.leasing.Car;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xholy1
 */
public class CarTableModel extends AbstractTableModel{

    private final List<Car> cars = new ArrayList<>();
    private final ResourceBundle texts;
    
    public CarTableModel(ResourceBundle texts){
        this.texts = texts;
        addExampleCars();
    }
    
    @Override
    public int getRowCount() {
        return cars.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return texts.getString("type");
            case 1:
                return texts.getString("vendor");
            case 2:
                return texts.getString("modelYear");
            case 3:
                return texts.getString("seats");
            case 4:
                return texts.getString("registrationPlate");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex<0 || rowIndex>= cars.size()){
            return null;
        }
        
        Car car = cars.get(rowIndex);
        
        switch(columnIndex){
            case 0: return car.getType();
            case 1: return car.getVendor();
            case 2: return car.getModelYear();
            case 3: return car.getSeats();
            case 4: return car.getRegistrationPlate();
            default: return null;
        }                
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 4:
                return String.class;
            case 2:
            case 3:
                return Integer.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addCar(Car car){
        cars.add(car);
        int lastRow = cars.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    public Car getSelectedCar(int row){
        if(row>=cars.size() || row<0)
            return null;
        return cars.get(row);
    }
    
    public Car getCarWithId(Long id){
        for(Car c: cars){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
    
    public List<Car> getCars(){
        return Collections.unmodifiableList(cars);
    }
    
    private void addExampleCars(){
        Car c = new Car();
        c.setId(1L);
        c.setType("Fabia");
        c.setModelYear(2009);
        c.setVendor("Škoda");
        c.setSeats(5);
        c.setRegistrationPlate("BKE 4535");
        addCar(c);
        c = new Car();
        c.setId(2L);
        c.setType("X6");
        c.setModelYear(2013);
        c.setVendor("BmW");
        c.setSeats(5);
        c.setRegistrationPlate("AND 5632");
        addCar(c);
    }
        
}
