package cz.muni.fi.car.leasing.gui;

import cz.muni.fi.car.leasing.Lease;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    
    public LeaseTableModel(CarTableModel carsModel, CustomerTableModel customersModel,
            ResourceBundle texts){
        this.texts = texts;
        this.carsModel = carsModel;
        this.customersModel = customersModel;
        addExampleLease();
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
        leases.add(lease);
        int lastRow = leases.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    public Lease getSelectedLease(int row){
        return leases.get(row);
    }
    
    private void addExampleLease(){
        Lease l = new Lease();
        l.setCarId(1L);
        l.setCustomerId(11L);
        l.setStartTime(LocalDateTime.parse("2001-05-06T10:00"));
        l.setExpectedEndTime(LocalDateTime.parse("2001-05-08T10:00"));
        l.setRealEndTime(LocalDateTime.parse("2001-05-08T13:00"));
        l.setPrice(new BigDecimal("2000"));
        l.setFee(new BigDecimal("200"));
        addLease(l);
        l = new Lease();
        l.setCarId(2L);
        l.setCustomerId(12L);
        l.setStartTime(LocalDateTime.parse("2001-05-06T10:00"));
        l.setExpectedEndTime(LocalDateTime.parse("2001-05-08T10:00"));
        l.setRealEndTime(LocalDateTime.parse("2001-05-08T13:00"));
        l.setPrice(new BigDecimal("4500"));
        l.setFee(new BigDecimal("499"));
        addLease(l);
        
    }
}
