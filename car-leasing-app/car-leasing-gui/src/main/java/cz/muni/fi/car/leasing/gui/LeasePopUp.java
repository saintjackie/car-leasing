package cz.muni.fi.car.leasing.gui;

import cz.muni.fi.car.leasing.Car;
import cz.muni.fi.car.leasing.CarManager;
import cz.muni.fi.car.leasing.CarManagerImpl;
import cz.muni.fi.car.leasing.Customer;
import cz.muni.fi.car.leasing.CustomerManager;
import cz.muni.fi.car.leasing.CustomerManagerImpl;
import cz.muni.fi.car.leasing.Lease;
import java.awt.Window;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * This file contains 1 jpanel class and 2 abstractListModel classes
 * @author Jackie
 */
public class LeasePopUp extends javax.swing.JPanel {

    private String action;
    private Lease lease;
    private ResourceBundle texts;
    private JTable table;
    private CustomerListModel customersModel = new CustomerListModel();
    private CarListModel carsModel = new CarListModel();
    
    /**
     * Creates new form LeasePopUp
     */
    public LeasePopUp(String action,Lease lease,
            ResourceBundle texts,JTable leaseTable,DataSource dataSource) {
        initComponents();
        this.action = action;
        this.lease = lease;
        this.texts = texts;
        this.table = leaseTable;
        CustomerManager customerManager = new CustomerManagerImpl(dataSource);
        CarManager carManager = new CarManagerImpl(dataSource);
        jList1.setModel(customersModel);
        jList2.setModel(carsModel);  
        customersModel.addCustomer(null);
        carsModel.addCar(null);
        //examples
        customersModel.addListOfCustomers(customerManager.findAll());
        carsModel.addListOfCars(carManager.findAll());
        
        switch(action){
            case "add":
                jLabel1.setText(texts.getString("addLease"));
                //set actual date      
                LocalDateTime t = LocalDateTime.now();
                setDateTimeToComboBox(jComboBox1, jComboBox2, jComboBox3, jComboBox4, jComboBox5,
                        t.getDayOfMonth(), t.getMonthValue(), t.getYear(),t.getHour(),t.getMinute());
                setDateTimeToComboBox(jComboBox6, jComboBox7, jComboBox8, jComboBox9, jComboBox10,
                        t.getDayOfMonth(), t.getMonthValue(), t.getYear(),t.getHour(),t.getMinute());
                break;
            case "edit":
                jLabel1.setText(texts.getString("editLease"));
                setTextsFromLease(lease);
                setSelectedCarAndCustomer(lease.getCustomerId(),lease.getCarId());
                break;
            case "filter":
                jLabel1.setText(texts.getString("filterLeases"));
                setTextsFromLease(lease);
                setSelectedCarAndCustomer(lease.getCustomerId(),lease.getCarId());
                break;
        }
        
    }
    
    private void setDateTimeToComboBox(JComboBox day, JComboBox month, JComboBox year,
            JComboBox hour,JComboBox minute,int dayVal,int monthVal,int yearVal,int hourVal,int minVal){
        day.setSelectedIndex(dayVal);
        month.setSelectedIndex(monthVal);
        year.setSelectedIndex(Year.now().getValue()-yearVal+1);
        hour.setSelectedIndex(hourVal+1);
        minute.setSelectedIndex(minVal+1);
    }
    
    private void setTextsFromLease(Lease l){
        //all values dont have to be setted cause filterCustomer can have all field null
        if(l.getStartTime() != null){
            setDateTimeToComboBox(jComboBox1, jComboBox2, jComboBox3, jComboBox4, jComboBox5,
                    l.getStartTime().getDayOfMonth(), l.getStartTime().getMonthValue(),
                    l.getStartTime().getYear(),l.getStartTime().getHour() ,l.getStartTime().getMinute());
        }
        if(l.getExpectedEndTime() != null){
            setDateTimeToComboBox(jComboBox6, jComboBox7, jComboBox8, jComboBox9, jComboBox10,
                    l.getExpectedEndTime().getDayOfMonth(), l.getExpectedEndTime().getMonthValue(),
                    l.getExpectedEndTime().getYear(),l.getExpectedEndTime().getHour(),
                    l.getExpectedEndTime().getMinute());
        }
        if(l.getRealEndTime() != null) {
            setDateTimeToComboBox(jComboBox11, jComboBox12, jComboBox13, jComboBox14, jComboBox15,
                    l.getRealEndTime().getDayOfMonth(), l.getRealEndTime().getMonthValue(),
                    l.getRealEndTime().getYear(),l.getRealEndTime().getHour(),
                    l.getRealEndTime().getMinute());
        }
        if(l.getPrice() != null)
            jTextField4.setText(l.getPrice().toString());
        if(l.getFee() != null)
            jTextField5.setText(l.getFee().toString());        
    }

    private void setSelectedCarAndCustomer(Long cusId,Long carId){
        if(cusId != null){
            jList1.setSelectedIndex(customersModel.getIndexOfCustomerWithId(cusId));
        }
        if(carId != null){
            jList2.setSelectedIndex(carsModel.getIndexOfCarWithId(carId));
        }
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        Integer[] days = new Integer[32];
        for(int i=1;i<32;i++) days[i] = i;
        jComboBox1 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        Integer[] months = new Integer[13];
        for(int i=1;i<13;i++) months[i] = i;
        jComboBox2 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        int currentYear = Year.now().getValue();
        Integer[] years = new Integer[currentYear-1899];
        for(int i=1;i<currentYear-1899;i++) years[i] = currentYear-i+1;
        jComboBox3 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        Integer[] hours = new Integer[25];
        for(int i=1;i<25;i++) hours[i] = i-1;
        jComboBox4 = new javax.swing.JComboBox();
        Integer[] minutes = new Integer[61];
        for(int i=1;i<61;i++) minutes[i] = i-1;
        jComboBox5 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jComboBox9 = new javax.swing.JComboBox();
        jComboBox10 = new javax.swing.JComboBox();
        jComboBox11 = new javax.swing.JComboBox();
        jComboBox12 = new javax.swing.JComboBox();
        jComboBox13 = new javax.swing.JComboBox();
        jComboBox14 = new javax.swing.JComboBox();
        jComboBox15 = new javax.swing.JComboBox();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("jLabel1");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("texts"); // NOI18N
        jLabel2.setText(bundle.getString("customer")); // NOI18N

        jLabel3.setText(bundle.getString("car")); // NOI18N

        jLabel4.setText(bundle.getString("start")); // NOI18N

        jLabel5.setText(bundle.getString("end")); // NOI18N

        jLabel6.setText(bundle.getString("realEnd")); // NOI18N

        jLabel7.setText(bundle.getString("price")); // NOI18N

        jLabel8.setText(bundle.getString("fee")); // NOI18N

        jButton1.setText(bundle.getString("ok")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(bundle.getString("cancel")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(days));

        jLabel9.setText(bundle.getString("day")); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(months));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel10.setText(bundle.getString("month")); // NOI18N

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(years));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel11.setText(bundle.getString("year")); // NOI18N

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(hours));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(minutes));

        jLabel12.setText(bundle.getString("hour")); // NOI18N

        jLabel13.setText(bundle.getString("minute")); // NOI18N

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(days));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(months));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(years));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(hours));

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(minutes));

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(days));

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(months));
        jComboBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox12ActionPerformed(evt);
            }
        });

        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel(years));
        jComboBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox13ActionPerformed(evt);
            }
        });

        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel(hours));

        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel(minutes));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1)
                                    .addComponent(jScrollPane2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                                .addComponent(jTextField4))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel9))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel10))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel11))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel12))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel13)
                                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        switch(action){
            case "add":
                addNewLease();
                break;
            case "edit":
                editLease();
                break;
            case "filter":
                filterLease();
                break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        if(jComboBox2.getSelectedIndex()>0 && jComboBox3.getSelectedIndex()>0){
            int selectedYear = (int) jComboBox3.getSelectedItem();
            int maxDaysOfMonth = YearMonth.of(selectedYear,(int)jComboBox2.getSelectedItem()).lengthOfMonth();
            while(maxDaysOfMonth+1 < jComboBox1.getModel().getSize()){//we must delete some days in jcombobox1
                jComboBox1.removeItemAt(jComboBox1.getModel().getSize()-1);
            }
            while(maxDaysOfMonth+1 > jComboBox1.getModel().getSize()){//we must add some days
                jComboBox1.addItem(jComboBox1.getModel().getSize());
            }
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        jComboBox2ActionPerformed(evt);
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        if(jComboBox7.getSelectedIndex()>0 && jComboBox8.getSelectedIndex()>0){
            int selectedYear = (int) jComboBox8.getSelectedItem();
            int maxDaysOfMonth = YearMonth.of(selectedYear,(int)jComboBox7.getSelectedItem()).lengthOfMonth();
            while(maxDaysOfMonth+1 < jComboBox6.getModel().getSize()){//we must delete some days in jcombobox1
                jComboBox6.removeItemAt(jComboBox6.getModel().getSize()-1);
            }
            while(maxDaysOfMonth+1 > jComboBox6.getModel().getSize()){//we must add some days
                jComboBox6.addItem(jComboBox6.getModel().getSize());
            }
        }
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        jComboBox7ActionPerformed(evt);
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox12ActionPerformed
        if(jComboBox12.getSelectedIndex()>0 && jComboBox13.getSelectedIndex()>0){
            int selectedYear = (int) jComboBox13.getSelectedItem();
            int maxDaysOfMonth = YearMonth.of(selectedYear,(int)jComboBox12.getSelectedItem()).lengthOfMonth();
            while(maxDaysOfMonth+1 < jComboBox11.getModel().getSize()){//we must delete some days in jcombobox1
                jComboBox11.removeItemAt(jComboBox11.getModel().getSize()-1);
            }
            while(maxDaysOfMonth+1 > jComboBox11.getModel().getSize()){//we must add some days
                jComboBox11.addItem(jComboBox11.getModel().getSize());
            }
        }
    }//GEN-LAST:event_jComboBox12ActionPerformed

    private void jComboBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox13ActionPerformed
        jComboBox12ActionPerformed(evt);
    }//GEN-LAST:event_jComboBox13ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox13;
    private javax.swing.JComboBox jComboBox14;
    private javax.swing.JComboBox jComboBox15;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

    private void addNewLease() {
        Lease newLease = new Lease();
        List<String> errors = checkCorrectnessOfTextInput();
        if(!errors.isEmpty()){
            JOptionPane.showMessageDialog(null,
                    texts.getString("fillUpAllFields") +": "+
                            errors.toString(),texts.getString("fillUpAllFields"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        fillLeaseFromTextInput(newLease);
        
        ((LeaseTableModel)table.getModel()).addLease(newLease);
        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
    }
    
    private void fillLeaseFromTextInput(Lease lease){
        Customer cus = customersModel.getSelectedCustomer(jList1.getSelectedIndex());
        if(cus != null){
            lease.setCustomerId(cus.getId());
        }
        Car car = carsModel.getSelectedCar(jList2.getSelectedIndex());
        if(car != null){
            lease.setCarId(car.getId());
        }
        lease.setStartTime(LocalDateTime.of((int)jComboBox3.getSelectedItem(),
                (int)jComboBox2.getSelectedItem(),(int)jComboBox1.getSelectedItem(),
                (int)jComboBox4.getSelectedItem(),(int)jComboBox5.getSelectedItem()));
        lease.setExpectedEndTime(LocalDateTime.of((int)jComboBox8.getSelectedItem(),
                (int)jComboBox7.getSelectedItem(),(int)jComboBox6.getSelectedItem(),
                (int)jComboBox9.getSelectedItem(),(int)jComboBox10.getSelectedItem()));
        
        if(jComboBox11.getSelectedIndex()>0){
            lease.setRealEndTime(LocalDateTime.of((int)jComboBox13.getSelectedItem(),
                (int)jComboBox12.getSelectedItem(),(int)jComboBox11.getSelectedItem(),
                (int)jComboBox14.getSelectedItem(),(int)jComboBox15.getSelectedItem()));
        }else{
            lease.setRealEndTime(null);            
        }
        lease.setPrice(new BigDecimal(jTextField4.getText().trim()));
        
        if(jTextField5.getText().trim().isEmpty()){
            lease.setFee(null);
        }else{
            lease.setFee(new BigDecimal(jTextField5.getText().trim()));
        }
        
    }

    private void editLease() {
        List<String> errors = checkCorrectnessOfTextInput();
        if(!errors.isEmpty()){
            JOptionPane.showMessageDialog(null,
                    texts.getString("fillUpAllFields") +": "+
                            errors.toString(),texts.getString("fillUpAllFields"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        fillLeaseFromTextInput(lease);
        int selectedRow = table.getSelectedRow();
        ((LeaseTableModel)table.getModel()).updateLease(lease, selectedRow);
        
        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
    }

    private void filterLease() {
        List<String> errors = checkCorrectnessOfTextInputForFiltering();
        if(!errors.isEmpty()){
            JOptionPane.showMessageDialog(null,
                    texts.getString("fillUpAllFields") +": "+
                            errors.toString(),texts.getString("fillUpAllFields"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        fillUpLeaseForFilterFromTextInput();
        
        ((LeaseTableModel)table.getModel()).filterLeases();
        table.updateUI();
        
        Window win = SwingUtilities.getWindowAncestor(this);
        win.dispose();
        
    }

    private List<String> checkCorrectnessOfTextInput() {
        List<String> errors = new ArrayList<>();
        if(jList1.getSelectedIndex()==-1 || jList1.getSelectedIndex()==0){
            errors.add(jLabel2.getText());
        }
        if(jList2.getSelectedIndex()==-1 || jList2.getSelectedIndex()==0){
            errors.add(jLabel3.getText());
        }
        if(jComboBox1.getSelectedIndex() < 1 || jComboBox2.getSelectedIndex() < 1 ||
                jComboBox3.getSelectedIndex() < 1 ||jComboBox4.getSelectedIndex() < 1 ||
                jComboBox5.getSelectedIndex() < 1){
            errors.add(jLabel4.getText());
        }
        if(jComboBox6.getSelectedIndex() < 1 || jComboBox7.getSelectedIndex() < 1 ||
                jComboBox8.getSelectedIndex() < 1 ||jComboBox9.getSelectedIndex() < 1 ||
                jComboBox10.getSelectedIndex() < 1){
            errors.add(jLabel5.getText());
        }
        if(jComboBox11.getSelectedIndex() > 0 ||jComboBox12.getSelectedIndex() > 0 ||
                jComboBox13.getSelectedIndex() > 0 ||jComboBox14.getSelectedIndex() > 0 ||
                jComboBox15.getSelectedIndex() > 0){            
            if(jComboBox11.getSelectedIndex() < 1 || jComboBox12.getSelectedIndex() < 1 ||
                    jComboBox13.getSelectedIndex() < 1 || jComboBox14.getSelectedIndex() < 1 ||
                    jComboBox15.getSelectedIndex() < 1) {
                errors.add(jLabel6.getText());
            }
        }
        if(!jTextField4.getText().trim().matches("\\d+")){
            errors.add(jLabel7.getText());
        }
        if(!jTextField5.getText().trim().isEmpty()){
            if(!jTextField5.getText().trim().matches("\\d+")){
                errors.add(jLabel8.getText());
            }
        }
        
        return errors;
    }  
    
    private List<String> checkCorrectnessOfTextInputForFiltering(){
        List<String> errors = new ArrayList<>();
        //startTime
        if(jComboBox1.getSelectedIndex() > 0 ||jComboBox2.getSelectedIndex() > 0 ||
                jComboBox3.getSelectedIndex() > 0 ||jComboBox4.getSelectedIndex() > 0 ||
                jComboBox5.getSelectedIndex() > 0){            
            if(jComboBox1.getSelectedIndex() < 1 || jComboBox2.getSelectedIndex() < 1 ||
                    jComboBox3.getSelectedIndex() < 1 || jComboBox4.getSelectedIndex() < 1 ||
                    jComboBox5.getSelectedIndex() < 1) {
                errors.add(jLabel4.getText());
            }
        }
        //expectedEndTime
        if(jComboBox6.getSelectedIndex() > 0 ||jComboBox7.getSelectedIndex() > 0 ||
                jComboBox8.getSelectedIndex() > 0 ||jComboBox9.getSelectedIndex() > 0 ||
                jComboBox10.getSelectedIndex() > 0){            
            if(jComboBox6.getSelectedIndex() < 1 || jComboBox7.getSelectedIndex() < 1 ||
                    jComboBox8.getSelectedIndex() < 1 || jComboBox9.getSelectedIndex() < 1 ||
                    jComboBox10.getSelectedIndex() < 1) {
                errors.add(jLabel5.getText());
            }
        }
        //realEndTime
        if(jComboBox11.getSelectedIndex() > 0 ||jComboBox12.getSelectedIndex() > 0 ||
                jComboBox13.getSelectedIndex() > 0 ||jComboBox14.getSelectedIndex() > 0 ||
                jComboBox15.getSelectedIndex() > 0){            
            if(jComboBox11.getSelectedIndex() < 1 || jComboBox12.getSelectedIndex() < 1 ||
                    jComboBox13.getSelectedIndex() < 1 || jComboBox14.getSelectedIndex() < 1 ||
                    jComboBox15.getSelectedIndex() < 1) {
                errors.add(jLabel6.getText());
            }
        }
        if(!jTextField4.getText().trim().isEmpty() && !jTextField4.getText().trim().matches("\\d+"))
            errors.add(jLabel7.getText());
        if(!jTextField5.getText().trim().isEmpty() && !jTextField5.getText().trim().matches("\\d+"))
            errors.add(jLabel8.getText());
        return errors;            
    }
        
    private void fillUpLeaseForFilterFromTextInput(){
        //customerId
        if(jList1.getSelectedIndex() > 0) {
            Customer cus = customersModel.getSelectedCustomer(jList1.getSelectedIndex());
            if(cus != null) {
                lease.setCustomerId(cus.getId());
            } else {
                lease.setCustomerId(null);
            }
        }else
            lease.setCustomerId(null);
        //carId
        if(jList2.getSelectedIndex() > 0) {
            Car car = carsModel.getSelectedCar(jList2.getSelectedIndex());
            if(car != null) {
                lease.setCarId(car.getId());
            } else {
                lease.setCarId(null);
            }
        }else
            lease.setCarId(null);
        
        //startTime
        if(jComboBox1.getSelectedIndex()>0)
            lease.setStartTime(LocalDateTime.of((int)jComboBox3.getSelectedItem(),
                (int)jComboBox2.getSelectedItem(),(int)jComboBox1.getSelectedItem(),
                (int)jComboBox4.getSelectedItem(),(int)jComboBox5.getSelectedItem()));
        else
            lease.setStartTime(null);
        //expectedEndTime
        if(jComboBox6.getSelectedIndex()>0)
            lease.setExpectedEndTime(LocalDateTime.of((int)jComboBox8.getSelectedItem(),
                (int)jComboBox7.getSelectedItem(),(int)jComboBox6.getSelectedItem(),
                (int)jComboBox9.getSelectedItem(),(int)jComboBox10.getSelectedItem()));
        else
            lease.setExpectedEndTime(null);
        //realEndTime
        if(jComboBox11.getSelectedIndex()>0)
            lease.setRealEndTime(LocalDateTime.of((int)jComboBox13.getSelectedItem(),
                (int)jComboBox12.getSelectedItem(),(int)jComboBox11.getSelectedItem(),
                (int)jComboBox14.getSelectedItem(),(int)jComboBox15.getSelectedItem()));
        else
            lease.setRealEndTime(null);
        //price
        if(!jTextField4.getText().trim().isEmpty() && jTextField4.getText().trim().matches("\\d+"))
            lease.setPrice(new BigDecimal(jTextField4.getText().trim()));
        else
            lease.setPrice(null);
        //fee
        if(!jTextField5.getText().trim().isEmpty() && jTextField5.getText().trim().matches("\\d+"))
            lease.setFee(new BigDecimal(jTextField5.getText().trim()));
        else
            lease.setFee(null);
        
    }
    

}

class CarListModel extends AbstractListModel{

    private final List<Car> cars = new ArrayList<>();
    
    @Override
    public int getSize() {
        return cars.size();
    }

    @Override
    public Object getElementAt(int index) {
        if(index > getSize()-1) 
            return null;
        return cars.get(index);
    }
    
    public void addCar(Car car){
        cars.add(car);        
        fireIntervalAdded(this, getSize()-1, getSize()-1);
    }
    
    public void addListOfCars(List<Car> list){
        int before = getSize();
        cars.addAll(list);
        fireIntervalAdded(this, before, getSize());
    }
    
    public Car getSelectedCar(int index){
        if(index > getSize()-1) 
            return null;
        return cars.get(index);
    }
    
    public int getIndexOfCar(Car car){
        return cars.indexOf(car);
    }
    
    public int getIndexOfCarWithId(Long id){
        int index=0;
        for(Car c: cars){
            if(c != null) {
                if(id.equals(c.getId())) {
                    return index;
                }
            }
            index++;
        }
        return 0;
    }
    
}

class CustomerListModel extends AbstractListModel{
    
    private final List<Customer> customers = new ArrayList<>();

    @Override
    public int getSize() {
        return customers.size();
    }

    @Override
    public Object getElementAt(int index) {
        if(index >= getSize() || index<0) 
            return null;
        return customers.get(index);
    }
    
    public void addCustomer(Customer customer){
        customers.add(customer);
        fireIntervalAdded(this, getSize()-1, getSize()-1);
    }
    
    public void addListOfCustomers(List<Customer> list){
        int before = getSize();
        customers.addAll(list);
        fireIntervalAdded(this, before, getSize());
    }
    
    public Customer getSelectedCustomer(int index) {
        if(index >= getSize() || index<0) 
            return null;
        return customers.get(index);
    }
    
    public int getIndexOfCustomer(Customer customer){
        return customers.indexOf(customer);
    }
    
    public int getIndexOfCustomerWithId(Long id){
        int index=0;
        for(Customer c: customers){
            if(c != null) {
                if(id.equals(c.getId())) {
                    return index;
                }
            }
            index++;
        }
        return 0;
    }
}