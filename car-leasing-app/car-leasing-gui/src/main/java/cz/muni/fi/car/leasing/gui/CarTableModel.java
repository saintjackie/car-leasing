package cz.muni.fi.car.leasing.gui;


import cz.muni.fi.car.leasing.Car;
import cz.muni.fi.car.leasing.CarManager;
import cz.muni.fi.car.leasing.CarManagerImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.sql.DataSource;
import javax.swing.*;
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
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                cars.addAll(carManager.findAll());
                return null;
            }
        };
        worker.execute();
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
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                carManager.create(car);
                return null;
            }

            @Override
            protected void done() {
                cars.add(car);
                int lastRow = cars.size() - 1;
                fireTableRowsInserted(lastRow, lastRow);
            }
        };
        worker.execute();
    }
    
    public void filterCars(MainFrame mf){
        SwingWorker<List<Car>, Void> worker = new SwingWorker<List<Car>, Void>() {
            @Override
            protected List<Car> doInBackground() throws Exception {
                List<Car> filteredCars = null;
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
                return filteredCars;
            }

            @Override
            protected void done() {
                try {
                    List<Car> filteredCars = get();
                    if(filteredCars!=null){
                        cars.clear();
                        cars.addAll(filteredCars);
                        filtered = true;
                    }else{
                        refresh();
                        filtered = false;
                    }
                    mf.setRemoveFilterButtonEnabled(filtered);
                    fireTableDataChanged();
                } catch(ExecutionException ex) {
                    // TODO DB error handling
                    JOptionPane.showMessageDialog(null,
                        "Connection to database failed",
                        "Filter error",
                        JOptionPane.WARNING_MESSAGE);
                } catch(InterruptedException ex) {
                    throw new RuntimeException("Operation interrupted (this should never happen)",ex);
                }
            }
        };
        worker.execute();
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
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                carManager.update(car);
                return null;
            }

            @Override
            protected void done() {
                fireTableRowsUpdated(selectedRow,selectedRow);
            }
        };
        worker.execute();
    }
    
    public void refresh(){
        cars.clear();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                cars.addAll(carManager.findAll());
                return null;
            }

            @Override
            protected void done() {
                fireTableDataChanged();
            }
        };
        worker.execute();
    }
    
    public void deleteCar(int row){
        SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                carManager.delete(cars.get(row).getId());
                return null;
            }
            @Override
            protected void done() {
                cars.remove(row);
                fireTableDataChanged();
            }
        };
        worker.execute();
    }
    
    public Car getSelectedCar(int row){
        if(row>=cars.size() || row<0)
            return null;
        return cars.get(row);
    }
    
}
