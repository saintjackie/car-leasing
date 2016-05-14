package cz.muni.fi.car.leasing.gui;

import cz.muni.fi.car.leasing.Car;
import cz.muni.fi.car.leasing.CarManager;
import cz.muni.fi.car.leasing.CarManagerImpl;
import cz.muni.fi.car.leasing.Customer;
import cz.muni.fi.car.leasing.CustomerManager;
import cz.muni.fi.car.leasing.CustomerManagerImpl;
import cz.muni.fi.car.leasing.Lease;
import cz.muni.fi.car.leasing.LeaseManager;
import cz.muni.fi.car.leasing.LeaseManagerImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jackie
 */
public class LeaseTableModel extends AbstractTableModel{
    
    private final List<Lease> leases = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final ResourceBundle texts;
    private final LeaseManager leaseManager;
    private final CarManager carManager;
    private final CustomerManager customerManager;
    private final Lease filterLease = new Lease();
    private boolean filtered=false;
    
    public LeaseTableModel(CarTableModel carsModel, CustomerTableModel customersModel,
            ResourceBundle texts,DataSource dataSource){
        this.texts = texts;
        leaseManager = new LeaseManagerImpl(dataSource);
        carManager = new CarManagerImpl(dataSource);
        customerManager = new CustomerManagerImpl(dataSource);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                leases.addAll(leaseManager.findAll());
                for(Lease l : leases){
                    cars.add(carManager.findById(l.getCarId()));
                    customers.add(customerManager.findById(l.getCustomerId()));
                }
                
                return null;
            }
        };
        worker.execute();
    }

    @Override
    public int getRowCount() {
        return leases.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return texts.getString("car");
            case 1:
                return texts.getString("customer");
            case 2:
                return texts.getString("start");
            case 3:
                return texts.getString("end");
            case 4:
                return texts.getString("realEnd");
            case 5:
                return texts.getString("price");
            case 6:
                return texts.getString("fee");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex<0 || rowIndex>= leases.size()){
            return null;
        }
        
        Lease lease = leases.get(rowIndex);
        
        switch(columnIndex){
            case 0:
                if(rowIndex <cars.size())
                    if(cars.get(rowIndex)!=null)
                        return cars.get(rowIndex).toString();
                return null;
            case 1: 
                if(rowIndex < customers.size())
                    if(customers.get(rowIndex)!=null)
                        return customers.get(rowIndex).toString();
                return null;
            case 2: return lease.getStartTime();
            case 3: return lease.getExpectedEndTime();
            case 4: return lease.getRealEndTime();
            case 5: return lease.getPrice();
            case 6: return lease.getFee();
            default: return null;
        }     
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return String.class;
            case 2:
            case 3:
            case 4:
                return LocalDateTime.class;
            case 5:
            case 6:
                return BigDecimal.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addLease(Lease lease){
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            Car car=null;
            Customer customer=null;
            @Override
            protected Void doInBackground() throws Exception {
                leaseManager.create(lease);
                car = carManager.findById(lease.getCarId());
                customer = customerManager.findById(lease.getCustomerId());
                return null;
            }

            @Override
            protected void done() {
                leases.add(lease);
                cars.add(car);
                customers.add(customer);
                int lastRow = leases.size() - 1;
                fireTableRowsInserted(lastRow, lastRow);
            }
        };
        worker.execute();
    }
    
    public void updateLease(Lease lease, int selectedRow){
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            Car car=null;
            Customer customer=null;
            @Override
            protected Void doInBackground() throws Exception {                
                leaseManager.update(lease);
                car = carManager.findById(lease.getCarId());                
                customer = customerManager.findById(lease.getCustomerId());                
                return null;
            }

            @Override
            protected void done() {                
                cars.set(selectedRow, car);
                customers.set(selectedRow, customer);
                fireTableDataChanged();
            }
        };
        worker.execute();
    }
    
    public void filterLeases(MainFrame mf){
        SwingWorker<List<Lease>, Void> worker = new SwingWorker<List<Lease>, Void>() {
            List<Car> filteredCars=new ArrayList<>();
            List<Customer> filteredCustomers=new ArrayList<>();
            @Override
            protected List<Lease> doInBackground() throws Exception {
                List<Lease> filteredLeases = null;
                //carId
                if(filterLease.getCarId() != null){
                    filteredLeases = leaseManager.findByCar(filterLease.getCarId());
                }
                //customerId
                if(filterLease.getCustomerId() != null){
                    if(filteredLeases == null)
                        filteredLeases = leaseManager.findByCustomer(filterLease.getCustomerId());
                    else
                        filteredLeases.retainAll(leaseManager.findByCustomer(filterLease.getCustomerId()));
                }
                //startTime
                if(filterLease.getStartTime() != null){
                    if(filteredLeases == null)
                        filteredLeases = leaseManager.findByStartTime(filterLease.getStartTime());
                    else
                        filteredLeases.retainAll(leaseManager.findByStartTime(filterLease.getStartTime()));
                }
                //expectedEndTime
                if(filterLease.getExpectedEndTime() != null){
                    if(filteredLeases == null)
                        filteredLeases = leaseManager.findByExpectedEndTime(filterLease.getExpectedEndTime());
                    else
                        filteredLeases.retainAll(leaseManager.findByExpectedEndTime(filterLease.getExpectedEndTime()));
                }
                //realEndTime
                if(filterLease.getRealEndTime() != null){
                    if(filteredLeases == null)
                        filteredLeases = leaseManager.findByRealEndTime(filterLease.getRealEndTime());
                    else
                        filteredLeases.retainAll(leaseManager.findByRealEndTime(filterLease.getRealEndTime()));
                }
                //price
                if(filterLease.getPrice() != null){
                    if(filteredLeases == null)
                        filteredLeases = leaseManager.findByPrice(filterLease.getPrice());
                    else
                        filteredLeases.retainAll(leaseManager.findByPrice(filterLease.getPrice()));
                }
                //fee
                if(filterLease.getFee() != null){
                    if(filteredLeases == null)
                        filteredLeases = leaseManager.findByFee(filterLease.getFee());
                    else
                        filteredLeases.retainAll(leaseManager.findByFee(filterLease.getFee()));
                }
                //fillup cars and cutomers according filteredLeases
                if(filteredLeases != null) {
                    for(Lease l : filteredLeases) {
                        filteredCars.add(carManager.findById(l.getCarId()));
                        filteredCustomers.add(customerManager.findById(l.getCustomerId()));
                    }
                }else{
                    filteredCars = null;
                    filteredCustomers = null;
                }
                return filteredLeases;
            }

            @Override
            protected void done() {
                try {
                    List<Lease> filteredLeases = get();
                    if (filteredLeases != null) {
                        leases.clear();
                        leases.addAll(filteredLeases);
                        cars.clear();
                        cars.addAll(filteredCars);
                        customers.clear();
                        customers.addAll(filteredCustomers);
                        filtered = true;
                    } else {
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
        filterLease.setCarId(null);
        filterLease.setCustomerId(null);
        filterLease.setStartTime(null);
        filterLease.setExpectedEndTime(null);
        filterLease.setRealEndTime(null);
        filterLease.setPrice(null);
        filterLease.setFee(null);
        filtered = false;        
    }
    
    public boolean isFiltered(){
        return filtered;
    }
    
    public Lease getFilterLease(){
        return filterLease;
    }
    
    public void refresh(){
        leases.clear();
        cars.clear();
        customers.clear();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                leases.addAll(leaseManager.findAll());
                for(Lease l : leases){
                    cars.add(carManager.findById(l.getCarId()));
                    customers.add(customerManager.findById(l.getCustomerId()));
                }
                return null;
            }
            
            @Override
            protected void done() {
                fireTableDataChanged();
            }
        };
        worker.execute();
    }
    
    public void deleteLease(int row){
        SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                leaseManager.delete(leases.get(row).getId());
                return null;
            }
            @Override
            protected void done() {
                leases.remove(row);
                cars.remove(row);
                customers.remove(row);
                fireTableDataChanged();
            }
        };
        worker.execute();
    }
    
    public Lease getSelectedLease(int row){
        return leases.get(row);
    }        
    
    public Car getSelectedCar(int row){
        return cars.get(row);
    }
    
    public Customer getSelectedCustomer(int row){
        return customers.get(row);
    }
    
    public void deleteCarWithId(Long id){
        for(int i=0;i<cars.size();i++){
            if(cars.get(i)!=null){
                if(cars.get(i).getId().equals(id)){
                    cars.set(i, null);
                }
            }
        }
    }
    
    public void deleteCustomerWithId(Long id){
        for(int i=0;i<customers.size();i++){
            if(customers.get(i)!=null){
                if(customers.get(i).getId().equals(id)){
                    customers.set(i, null);
                }
            }
        }
    }
}
