/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.car.leasing.gui;


import cz.muni.fi.car.leasing.Car;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xholy1
 */
public class CarTableModel extends AbstractTableModel{

    private List<Car> cars = new ArrayList<>();
    private ResourceBundle text;
    
    public CarTableModel(ResourceBundle texts){
        
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
        
}
