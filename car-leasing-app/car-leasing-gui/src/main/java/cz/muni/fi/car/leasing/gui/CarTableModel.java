package cz.muni.fi.car.leasing.gui;


import cz.muni.fi.car.leasing.Car;
import cz.muni.fi.car.leasing.CarManager;
import cz.muni.fi.car.leasing.CarManagerImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xholy1
 */
public class CarTableModel extends AbstractTableModel{

    private final List<Car> cars = new ArrayList<>();
    private final ResourceBundle texts;
    private final CarManager carManager;
    private final Car filterCar = new Car();    
    private boolean filtered = false;
    
    public CarTableModel(ResourceBundle texts,DataSource dataSource){
        this.texts = texts;        
        this.carManager = new CarManagerImpl(dataSource);
        cars.addAll(carManager.findAll());        
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
        carManager.create(car);
        cars.add(car);
        int lastRow = cars.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void filterCars(){       
        List<Car> filteredCars=null;
         //helping list, cause if nothing is find, retain wont intersect the list
        //type
        if(filterCar.getType() != null){
           filteredCars = carManager.findByType(filterCar.getType());
        }
        //vendor
        if(filterCar.getVendor() != null){
            if(filteredCars==null)
                filteredCars = carManager.findByVendor(filterCar.getVendor());
            else{
                filteredCars.retainAll(carManager.findByVendor(filterCar.getVendor()));
            }
        }
        //seats
        if(filterCar.getSeats() != null && filterCar.getSeats() > 0){
            if(filteredCars==null)
                filteredCars = carManager.findBySeats(filterCar.getSeats());
            else{
                filteredCars.retainAll(carManager.findBySeats(filterCar.getSeats()));
            }
        }
        //modelYear
        if(filterCar.getModelYear()!= null){
            if(filteredCars==null)
                filteredCars = carManager.findByModelYear(filterCar.getModelYear());
            else{
                filteredCars.retainAll(carManager.findByModelYear(filterCar.getModelYear()));               
            }
        }        
        //registrationPlate
        if(filterCar.getRegistrationPlate() != null){
            if(filteredCars==null){
                filteredCars = new ArrayList<>();
                Car c = carManager.findByRegistration(filterCar.getRegistrationPlate());
                if(c != null) 
                    filteredCars.add(c);
            }            
            else{
                filteredCars.retainAll((Collection<?>) carManager.findByRegistration(filterCar.getRegistrationPlate()));
            }
        }
        if(filteredCars!=null){
            cars.clear();
            cars.addAll(filteredCars);
            filtered = true;
        }else{
            refresh();
            filtered = false;
        }
    }
    
    public void removeFilter(){
        refresh();
        filterCar.setType(null);
        filterCar.setVendor(null);
        filterCar.setModelYear(null);
        filterCar.setSeats(null);
        filterCar.setRegistrationPlate(null);
        filtered = false;        
    }
    
    public boolean isFiltered(){
        return filtered;
    }
    
    public Car getFilterCar(){
        return filterCar;
    }
    
    public void updateCar(Car car, int selectedRow){
        carManager.update(car);
        fireTableRowsUpdated(selectedRow,selectedRow);
    }
    
    public void refresh(){
        cars.clear();
        cars.addAll(carManager.findAll());
        fireTableDataChanged();
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
        //if doesnt find in list cars, search in database
        return carManager.findById(id);
    }    
        
}
