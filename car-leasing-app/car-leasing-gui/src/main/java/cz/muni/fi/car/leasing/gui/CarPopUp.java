package cz.muni.fi.car.leasing.gui;

import cz.muni.fi.car.leasing.Car;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jackie
 */
public class CarPopUp extends javax.swing.JPanel {

    private String action;
    private Car car;
    private ResourceBundle texts;
    private JTable table;
    /**
     * Creates new form NewJPanel
     */
    public CarPopUp(String action,Car car,ResourceBundle texts,JTable table) {
        initComponents();
        this.action = action;
        this.car = car;
        this.texts = texts;
        this.table = table;        
        switch(action){
            case "add":
                jLabel1.setText(texts.getString("addCar"));
                break;
            case "edit":
                jLabel1.setText(texts.getString("editCar"));
                setTextsFromCar(car);
                break;
            case "filter": 
                jLabel1.setText(texts.getString("filterCars"));
                setTextsFromCar(car);
                break;                
        }  
    }
    
    private void setTextsFromCar(Car car){
        jTextField1.setText(car.getType());
        jTextField2.setText(car.getVendor());
        if(car.getSeats() != null)
            jSpinner1.setValue(car.getSeats());
        else
            jSpinner1.setValue(0);
        if(car.getModelYear() != null)
            jTextField3.setText(car.getModelYear().toString());
        jTextField4.setText(car.getRegistrationPlate());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pridat auto");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("texts"); // NOI18N
        jLabel2.setText(bundle.getString("type")); // NOI18N

        jLabel3.setText(bundle.getString("vendor")); // NOI18N

        jLabel4.setText(bundle.getString("seats")); // NOI18N

        jLabel5.setText(bundle.getString("modelYear")); // NOI18N

        jButton1.setText(bundle.getString("ok")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField3.setToolTipText(bundle.getString("tooltipModelYear")); // NOI18N

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(5, 0, 99, 1));

        jLabel6.setText(bundle.getString("registrationPlate")); // NOI18N

        jButton2.setText(bundle.getString("cancel")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2)
                            .addComponent(jTextField1)
                            .addComponent(jTextField4)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(13, 13, 13))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap(17, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        switch(action){
            case "add":
                addNewCar();
                break;
            case "edit":
                editCar();
                break;
            case "filter": 
                filterCar();
                break;
        } 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables

    private void addNewCar() {
        car = new Car();
        //check text input
        List<String> errors = checkCorrectnessOfTextInput();
        if(!errors.isEmpty()){
            JOptionPane.showMessageDialog(null,
                    texts.getString("fillUpAllFields") +": "+
                            errors.toString(),texts.getString("fillUpAllFields"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        fillUpCarFromTextInput();
        
        //add to table model
        ((CarTableModel)table.getModel()).addCar(car);
        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
    }

    private void editCar() {
        List<String> errors = checkCorrectnessOfTextInput();
        if(!errors.isEmpty()){
            JOptionPane.showMessageDialog(null,
                    texts.getString("fillUpAllFields") +": "+
                            errors.toString(),texts.getString("fillUpAllFields"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        fillUpCarFromTextInput();
        int selectedRow = table.getSelectedRow();
        ((CarTableModel)table.getModel()).updateCar(car, selectedRow);
        
        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
    }

    private void filterCar() {
        if(!jTextField3.getText().trim().isEmpty() &&!jTextField3.getText().trim().matches("\\d{4}")){
            JOptionPane.showMessageDialog(null,
                    texts.getString("fillUpAllFields") +": "+
                            jLabel5.getText(),texts.getString("fillUpAllFields"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        fillUpCarForFilteringFromTextInput();
        //table model filter
        Window w = SwingUtilities.getWindowAncestor(SwingUtilities.getWindowAncestor(this));
        ((CarTableModel) table.getModel()).filterCars((MainFrame)w);

        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
    }

    private List<String> checkCorrectnessOfTextInput() {
        List<String> errors = new ArrayList<>();

        if(jTextField1.getText().trim().isEmpty()){
            errors.add(jLabel2.getText());
        }
        if(jTextField2.getText().trim().isEmpty()){
            errors.add(jLabel3.getText());
        }
        if((int)jSpinner1.getValue() <= 0 || (int)jSpinner1.getValue() > 99){
            errors.add(jLabel4.getText());
        }
        if(!jTextField3.getText().trim().matches("\\d{4}")){
            errors.add(jLabel5.getText());
        } else {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int modelYear = Integer.valueOf(jTextField3.getText().trim());
            if (modelYear > currentYear) {
                errors.add(jLabel5.getText());
            }
        }
        if(jTextField4.getText().trim().isEmpty()){
            errors.add(jLabel6.getText());
        }
        return errors;
    }
    private void fillUpCarFromTextInput() {
        car.setType(jTextField1.getText().trim());
        car.setVendor(jTextField2.getText().trim());
        car.setSeats((int)jSpinner1.getValue());
        car.setModelYear(Integer.valueOf(jTextField3.getText().trim()));
        car.setRegistrationPlate(jTextField4.getText().trim());
    }
    
    private void fillUpCarForFilteringFromTextInput(){
        if(!jTextField1.getText().trim().isEmpty())
            car.setType(jTextField1.getText().trim());
        else
            car.setType(null);
        if(!jTextField2.getText().trim().isEmpty())
            car.setVendor(jTextField2.getText().trim());
        else
            car.setVendor(null);
        if((int)jSpinner1.getValue() != 0)
            car.setSeats((int)jSpinner1.getValue());
        else
            car.setSeats(null);
        if(!jTextField3.getText().trim().isEmpty() && jTextField3.getText().trim().matches("\\d{4}"))
            car.setModelYear(Integer.valueOf(jTextField3.getText().trim()));
        else
            car.setModelYear(null);
        if(!jTextField4.getText().trim().isEmpty())
            car.setRegistrationPlate(jTextField4.getText().trim());
        else
            car.setRegistrationPlate(null);
        
    }
}
