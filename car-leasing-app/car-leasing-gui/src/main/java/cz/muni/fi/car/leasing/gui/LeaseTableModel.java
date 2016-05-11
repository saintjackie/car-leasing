package cz.muni.fi.car.leasing.gui;

import cz.muni.fi.car.leasing.Lease;
import cz.muni.fi.car.leasing.LeaseManager;
import cz.muni.fi.car.leasing.LeaseManagerImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jackie
 */
public class LeaseTableModel extends AbstractTableModel{
    
    private final List<Lease> leases = new ArrayList<>();
    private final ResourceBundle texts;
    private final CarTableModel carsModel;
    private final CustomerTableModel customersModel;
    private final LeaseManager leaseManager;
    private final Lease filterLease = new Lease();
    private boolean filtered=false;
    
    public LeaseTableModel(CarTableModel carsModel, CustomerTableModel customersModel,
            ResourceBundle texts,DataSource dataSource){
        this.texts = texts;
        this.carsModel = carsModel;
        this.customersModel = customersModel;
        leaseManager = new LeaseManagerImpl(dataSource);
        leases.addAll(leaseManager.findAll());
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
            case 0: return carsModel.getCarWithId(lease.getCarId());
            case 1: return customersModel.getCustomerWithId(lease.getCustomerId());
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
        leaseManager.create(lease);
        leases.add(lease);
        int lastRow = leases.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void updateLease(Lease lease, int selectedRow){
        leaseManager.update(lease);
        fireTableRowsUpdated(selectedRow, selectedRow);
    }
    
    public void filterLeases(){
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
        
        if(filteredLeases !=null){
            leases.clear();
            leases.addAll(filteredLeases);
            filtered = true;
        }else{
            refresh();
            filtered = false;
        }
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
        leases.addAll(leaseManager.findAll());
    }
    
    public Lease getSelectedLease(int row){
        return leases.get(row);
    }        
}
