
package cz.muni.fi.car.leasing.gui;

import cz.muni.fi.car.leasing.Customer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jackie
 */
public class CustomerTableModel extends AbstractTableModel{
    
    private final List<Customer> customers = new ArrayList<>();
    private final ResourceBundle texts;
    
    public CustomerTableModel(ResourceBundle texts){
        this.texts = texts;  
        addExampleCustomers();
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return texts.getString("fullName");
            case 1:
                return texts.getString("phoneNumber");
            case 2:
                return texts.getString("birthDate");
            case 3:
                return texts.getString("address");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex<0 || rowIndex>= customers.size()){
            return null;
        }
        
        Customer customer = customers.get(rowIndex);
        
        switch(columnIndex){
            case 0: return customer.getFullName();
            case 1: return customer.getPhoneNumber();
            case 2: return customer.getBirthDate();
            case 3: return customer.getAddress();
            default: return null;
        }     
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 3:
                return String.class;
            case 2:
                return LocalDate.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addCustomer(Customer customer){
        customers.add(customer);
        int lastRow = customers.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    public Customer getSelectedCustomer(int row){
        if(row>=customers.size() || row <0)
            return null;
        return customers.get(row);
    }
    
    public Customer getCustomerWithId(Long id){
        for(Customer c: customers){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
    
    public List<Customer> getCustomers(){
        return Collections.unmodifiableList(customers);
    }
    
    
    private void addExampleCustomers(){
        Customer c = new Customer();
        c.setId(11L);
        c.setFullName("Jarda Nový");
        c.setPhoneNumber("516 456 678");
        c.setBirthDate(LocalDate.parse("1979-06-19"));
        c.setAddress("Kryštofova 124, Olomouc");
        addCustomer(c);
        c = new Customer();
        c.setId(12L);
        c.setFullName("Milada Nová");
        c.setPhoneNumber("516 123 987");
        c.setBirthDate(LocalDate.parse("1984-12-30"));
        c.setAddress("Úzká 987, Brno");
        addCustomer(c);
    }
    
}
