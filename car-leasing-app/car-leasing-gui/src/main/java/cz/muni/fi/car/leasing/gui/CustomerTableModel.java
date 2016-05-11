
package cz.muni.fi.car.leasing.gui;

import cz.muni.fi.car.leasing.Customer;
import cz.muni.fi.car.leasing.CustomerManager;
import cz.muni.fi.car.leasing.CustomerManagerImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jackie
 */
public class CustomerTableModel extends AbstractTableModel{
    
    private final List<Customer> customers = new ArrayList<>();
    private final ResourceBundle texts;
    private final CustomerManager customerManager;
    private final Customer filterCustomer=new Customer();
    private boolean filtered=false;
    
    public CustomerTableModel(ResourceBundle texts,DataSource dataSource){
        this.texts = texts;  
        customerManager =  new CustomerManagerImpl(dataSource);
        customers.addAll(customerManager.findAll());
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
        customerManager.create(customer);
        
        customers.add(customer);
        int lastRow = customers.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void updateCustomer(Customer customer, int selectedRow){
        customerManager.update(customer);
        fireTableRowsUpdated(selectedRow,selectedRow);
    }
    
    public void filterCustomers(){
        List<Customer> filteredCustomers = null;
        //fullname
        if(filterCustomer.getFullName()!= null){
            filteredCustomers = customerManager.findByName(filterCustomer.getFullName());                
        }
        //phoneNumber
        if(filterCustomer.getPhoneNumber()!= null){
            if(filteredCustomers == null)
                filteredCustomers = customerManager.findByPhoneNumber(filterCustomer.getPhoneNumber());
            else
                filteredCustomers.retainAll(customerManager.findByPhoneNumber(filterCustomer.getPhoneNumber()));
        }
        //birthDate
        if(filterCustomer.getBirthDate()!= null){
            if(filteredCustomers == null)
                filteredCustomers = customerManager.findByBirthDate(filterCustomer.getBirthDate());
            else
                filteredCustomers.retainAll(customerManager.findByBirthDate(filterCustomer.getBirthDate()));
        }
        //address
        if(filterCustomer.getAddress() != null){
            if(filteredCustomers == null)
                filteredCustomers = customerManager.findByAddress(filterCustomer.getAddress());
            else
                filteredCustomers.retainAll(customerManager.findByAddress(filterCustomer.getAddress()));
        }
        
        if(filteredCustomers != null){
            customers.clear();
            customers.addAll(filteredCustomers);
            filtered = true;
        }else{
            refresh();
            filtered = false;
        }
            
    }
    
    public void removeFilter(){
        refresh();
        filterCustomer.setFullName(null);
        filterCustomer.setPhoneNumber(null);
        filterCustomer.setBirthDate(null);
        filterCustomer.setAddress(null);
        filtered = false;        
    }
    
    public boolean isFiltered(){
        return filtered;
    }
    
    public Customer getFilterCustomer(){
        return filterCustomer;
    }
    
    public void refresh(){
        customers.clear();
        customers.addAll(customerManager.findAll());
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
        return customerManager.findById(id);
    }        
    
}
