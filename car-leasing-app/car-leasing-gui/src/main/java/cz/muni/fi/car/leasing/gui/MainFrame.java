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
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.dbcp2.BasicDataSource;


/**
 *
 * @author Jakub Holy
 */
public class MainFrame extends javax.swing.JFrame {
    
    private DataSource dataSource;

    private final ResourceBundle texts = ResourceBundle.getBundle("texts");
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {

        DataSourceSwingWorker dataSourceSwingWorker = new DataSourceSwingWorker();
        dataSourceSwingWorker.execute();
    }

    private class DataSourceSwingWorker extends SwingWorker<DataSource,Void> {

        @Override
        protected DataSource doInBackground() throws Exception {
            return setDataSource();
        }

        @Override
        protected void done() {
            try {                
                dataSource = get();
                initComponents();
                jTabbedPane1.setTitleAt(0, texts.getString("cars"));
                jTabbedPane1.setTitleAt(1, texts.getString("customers"));
                jTabbedPane1.setTitleAt(2, texts.getString("leasings"));
                setDeleteAndEditButtonEnabled(false);

                jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setListSelectionListeners();
                
                jButton3.setEnabled(false);
                jMenuItem6.setEnabled(false);
                setLocationRelativeTo(null);
            } catch (ExecutionException ex) {
                // TODO jTextArea.append("Exception thrown in doInBackground(): " + ex.getCause() + "\n");
                JOptionPane.showMessageDialog(null,
                        "Connection to database failed, aplication is closing.",
                        "Database error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Operation interrupted (this should never happen)",ex);
            }
        }
    }

    public DataSource setDataSource() {
        Properties dbconf = new Properties();
        try {
            dbconf.load(MainFrame.class.getResourceAsStream("/dbconf.properties"));
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(dbconf.getProperty("jdbc.url"));
        ds.setUsername(dbconf.getProperty("jdbc.user"));
        ds.setPassword(dbconf.getProperty("jdbc.password"));
 
        try(Connection con = ds.getConnection()) {
            //check if table exists, cause sql command "IF NOT EXISTS" doesnt work
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, null,null,null);
            while (rs.next()) { //check only if car exists => others exists too
                if(rs.getString(3).equals("CAR")) return ds;
            }
            //create tables
            StringBuilder sb= new StringBuilder("");         
            for (String line : Files.readAllLines(Paths.get("src", "main", "resources", "sqltables.sql"))) {                
                if(line.trim().isEmpty()) continue;
                sb.append(line.trim());
                if(line.endsWith(";")) {
                    sb.deleteCharAt(sb.length()-1); //delete ;
                    try(PreparedStatement st1 = con.prepareStatement(sb.toString())) {
                        st1.execute();
                    }
                    sb.setLength(0); //clearing sb
                }
            }
        } catch (SQLException|IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ds;
    }
    
    private void setListSelectionListeners(){
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        if (!lse.getValueIsAdjusting()) {
                            if(jTable1.getSelectedRow()<0)
                                setDeleteAndEditButtonEnabled(false);
                            else
                                setDeleteAndEditButtonEnabled(true);                            
                        }
                    }
                });
        jTable2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        if (!lse.getValueIsAdjusting()) {
                            if(jTable2.getSelectedRow()<0)
                                setDeleteAndEditButtonEnabled(false);
                            else
                                setDeleteAndEditButtonEnabled(true);                            
                        }
                    }
                });
        jTable3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        if (!lse.getValueIsAdjusting()) {
                            if(jTable3.getSelectedRow()<0)
                                setDeleteAndEditButtonEnabled(false);
                            else
                                setDeleteAndEditButtonEnabled(true);                            
                        }
                    }
                });
    }
    
    private void setDeleteAndEditButtonEnabled(boolean value){
            jButton6.setEnabled(value);
            jMenuItem3.setEnabled(value);
            jButton1.setEnabled(value);
            jMenuItem1.setEnabled(value);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("texts"); // NOI18N
        setTitle(bundle.getString("carLeasing")); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 600));
        setSize(new java.awt.Dimension(800, 600));

        jToolBar1.setRollover(true);

        jButton1.setText(bundle.getString("edit")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setText(bundle.getString("filter")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setText(bundle.getString("cancelFilter")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setText(bundle.getString("add")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton6.setText(bundle.getString("delete")); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton5.setText("Add example data");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jTable1.setModel(new CarTableModel(texts,dataSource));
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane1.addTab("Auta", jScrollPane1);

        jTable2.setModel(new CustomerTableModel(texts,dataSource));
        jScrollPane2.setViewportView(jTable2);

        jTabbedPane1.addTab("Zakaznici", jScrollPane2);

        jTable3.setModel(new LeaseTableModel((CarTableModel)jTable1.getModel(),(CustomerTableModel)jTable2.getModel(),texts,dataSource));
        jScrollPane3.setViewportView(jTable3);

        jTabbedPane1.addTab("Pronajem", jScrollPane3);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText(bundle.getString("file")); // NOI18N

        jMenuItem4.setText(bundle.getString("exit")); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(bundle.getString("action")); // NOI18N

        jMenuItem1.setText(bundle.getString("edit")); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText(bundle.getString("filter")); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem6.setText(bundle.getString("cancelFilter")); // NOI18N
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText(bundle.getString("add")); // NOI18N
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem3.setText(bundle.getString("delete")); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        getAccessibleContext().setAccessibleName("Vypujcka aut");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //add
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: 
                newAddJDialog(0);
                break;
            case 1:
                newAddJDialog(1);
                break;
            case 2:
                newAddJDialog(2);
                break;
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void newAddJDialog(int tabbedPaneIndex){ //0=car,1=customer,2=lease
        JDialog jDialog = new JDialog(this,true);
        switch(tabbedPaneIndex){
            case 0: //car pane
                jDialog.setTitle(texts.getString("addCar"));
                jDialog.getContentPane().add(new CarPopUp("add",null,texts,jTable1));
                break;
            case 1: //customer pane
                jDialog.setTitle(texts.getString("addCustomer"));
                jDialog.getContentPane().add(new CustomerPopUp("add",null,texts,jTable2));
                break;
            case 2: //lease pane
                jDialog.setTitle(texts.getString("addLease"));
                jDialog.getContentPane().add(new LeasePopUp("add",null,texts,jTable3,dataSource));
                break;                        
        }
   
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setResizable(false);
        jDialog.setVisible(true);
    }
    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //edit
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: //car pane
                int carSelectedRow = jTable1.getSelectedRow();
                if(carSelectedRow == -1) //no car selected
                    return;                
                newEditJDialog(0,carSelectedRow);
                break;
            case 1: //customer pane               
                int customerSelectedRow = jTable2.getSelectedRow();
                if(customerSelectedRow == -1) //no customer selected
                    return; 
                newEditJDialog(1,customerSelectedRow);
                break;
            case 2: //lease pane
                int leaseSelectedRow = jTable3.getSelectedRow();
                if(leaseSelectedRow == -1) //no lease selected
                    return;
                newEditJDialog(2,leaseSelectedRow);
                break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        jButton1ActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        jButton4ActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //add example data        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {            
            @Override
            protected Void doInBackground() throws Exception {
                Random rand = new Random();
                CarManager carManager = new CarManagerImpl(dataSource);
                CustomerManager customerManager = new CustomerManagerImpl(dataSource);
                LeaseManager leaseManager = new LeaseManagerImpl(dataSource);
                //cars
                String[] types = new String[]{"Superb","X6","Megane","Evolution","Focus","i30","a6"};
                String[] vendors = new String[]{"Škoda","BMW","Renault","Mitsubishi","Ford","Huyndai","Audi"};
                int[] modelYears = new int[]{2006,2007,2008,2009,2010,2011,2012};
                String[] regisPlates = new String[]{"ANO 1234","NEE 9876","KIN 0001","SIL 3467","BON 4598","SUN 1246","UFO 4577"};
                int[] seats = new int[]{4,5,6,2,4,5,6};
                List<Car> cars = new ArrayList<>();
                Car c;
                for(int i=0;i<7;i++){
                    c = new Car();
                    c.setType(types[i]);
                    c.setVendor(vendors[i]);
                    c.setSeats(seats[i]);
                    c.setModelYear(modelYears[i]);
                    c.setRegistrationPlate(regisPlates[i]);
                    cars.add(c);
                    carManager.create(c);
                }       
                //customers
                String[] fullNames = new String[]{"Jaromír Pažitka","Pavel Brambora","Zuzana Petržel","Vanesa Kmín"};
                String[] phones = new String[]{"123 456 678","987 654 321","222 333 444","111 999 000","234 977 009"};
                String[] births = new String[]{"1987-09-03","1990-11-23","1975-07-09","1968-01-27"};
                String[] addresses = new String[]{"Olša 346, Olomouc","Trnitá 2367, Praha","Férová 125, Brno","Lesná 346, Ostrava"};
                Customer cus;
                List<Customer> customers = new ArrayList<>();
                for(int i=0;i<4;i++){
                    cus = new Customer();
                    cus.setFullName(fullNames[i]);
                    cus.setPhoneNumber(phones[i]);
                    cus.setBirthDate(LocalDate.parse(births[i]));
                    cus.setAddress(addresses[i]);
                    customers.add(cus);
                    customerManager.create(cus);
                }
                //leases
                String[] dateTimes = new String[]{"2016-01-02T10:00","2016-05-26T12:00","2016-03-07T13:00","2015-12-23T12:00","2015-09-12T14:00"};
                String[] prices = new String[]{"1999","2999","2239","999","487","6500","8000"};
                String[] fees = new String[]{"299","199","99","1000","1099"};
                Lease l;
                for(int i=0;i<4;i++){
                    l = new Lease();
                    l.setCarId((cars.get(rand.nextInt(cars.size())).getId()));
                    l.setCustomerId(customers.get(rand.nextInt(customers.size())).getId());
                    l.setStartTime(LocalDateTime.parse(dateTimes[rand.nextInt(dateTimes.length)]));
                    l.setExpectedEndTime(LocalDateTime.parse(dateTimes[rand.nextInt(dateTimes.length)]));
                    l.setRealEndTime(LocalDateTime.parse(dateTimes[rand.nextInt(dateTimes.length)]));
                    l.setPrice(new BigDecimal(prices[rand.nextInt(prices.length)]));
                    l.setFee(new BigDecimal(fees[rand.nextInt(fees.length)]));            
                    leaseManager.create(l);
                }
                return null;
            }
            
            @Override
            protected void done() {
                ((CarTableModel)jTable1.getModel()).refresh();
                ((CustomerTableModel)jTable2.getModel()).refresh();
                ((LeaseTableModel)jTable3.getModel()).refresh();
            }
        };
        worker.execute();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //filter
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: //car pane
                newFilterJDialog(0);
                setRemoveFilterButtonEnabled(((CarTableModel)jTable1.getModel()).isFiltered());
                break;
            case 1: //customer pane               
                newFilterJDialog(1);
                setRemoveFilterButtonEnabled(((CustomerTableModel)jTable2.getModel()).isFiltered());
                break;
            case 2: //lease pane
                newFilterJDialog(2);
                setRemoveFilterButtonEnabled(((LeaseTableModel)jTable3.getModel()).isFiltered());
                break;
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: //car pane
                ((CarTableModel)jTable1.getModel()).removeFilter();
                jTable1.updateUI();
                setRemoveFilterButtonEnabled(false);
                break;
            case 1: //customer pane
                ((CustomerTableModel)jTable2.getModel()).removeFilter();
                jTable2.updateUI();
                setRemoveFilterButtonEnabled(false);
                break;
            case 2: //lease pane
                ((LeaseTableModel)jTable3.getModel()).removeFilter();
                jTable3.updateUI();
                setRemoveFilterButtonEnabled(false);
                break;
        }
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: //car pane
                setRemoveFilterButtonEnabled(((CarTableModel)jTable1.getModel()).isFiltered());
                if(jTable1.getSelectedRow()<0)
                    setDeleteAndEditButtonEnabled(false);
                else
                    setDeleteAndEditButtonEnabled(true);
                break;
            case 1: //customer pane
                setRemoveFilterButtonEnabled(((CustomerTableModel)jTable2.getModel()).isFiltered());
                if(jTable2.getSelectedRow()<0)
                    setDeleteAndEditButtonEnabled(false);
                else
                    setDeleteAndEditButtonEnabled(true);
                break;
            case 2: //lease pane
                setRemoveFilterButtonEnabled(((LeaseTableModel)jTable3.getModel()).isFiltered());
                if(jTable3.getSelectedRow()<0)
                    setDeleteAndEditButtonEnabled(false);
                else
                    setDeleteAndEditButtonEnabled(true);
                break;
        }
        
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        jButton2ActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        jButton3ActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //delete
        String[] options = new String[]{texts.getString("ok"),texts.getString("cancel")}; 
        int row,choice;
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: //car pane                               
                choice = JOptionPane.showOptionDialog(null,
                        texts.getString("deleteCarMessage"),
                        texts.getString("delete"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if(choice==0){
                    //delete from list of cars in LeaseTableModel
                    Car c = ((CarTableModel)jTable1.getModel()).getSelectedCar(jTable1.getSelectedRow());
                    ((LeaseTableModel)jTable3.getModel()).deleteCarWithId(c.getId());
                    //delete from database
                    ((CarTableModel)jTable1.getModel()).deleteCar(jTable1.getSelectedRow());                    
                }
                break;
            case 1: //customer pane
                choice = JOptionPane.showOptionDialog(null,
                        texts.getString("deleteCustomerMessage"),
                        texts.getString("delete"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if(choice==0){
                    //delete from list of customers in LeaseTableModel
                    Customer cus = ((CustomerTableModel)jTable2.getModel()).getSelectedCustomer(jTable2.getSelectedRow());
                    ((LeaseTableModel)jTable3.getModel()).deleteCustomerWithId(cus.getId());
                    //delete from database
                    ((CustomerTableModel)jTable2.getModel()).deleteCustomer(jTable2.getSelectedRow());
                }
                break;
            case 2: //lease pane
                row = jTable3.getSelectedRow();
                Lease l = ((LeaseTableModel)jTable3.getModel()).getSelectedLease(row);
                choice = JOptionPane.showOptionDialog(null,
                        texts.getString("deleteLeaseMessage"),
                        texts.getString("delete"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if(choice==0)
                    ((LeaseTableModel)jTable3.getModel()).deleteLease(jTable3.getSelectedRow());
                break;
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        jButton6ActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    public void setRemoveFilterButtonEnabled(boolean val){
        jButton3.setEnabled(val);
        jMenuItem6.setEnabled(val);
    }
    
    private void newEditJDialog(int tabbedPaneIndex, int selectedRow) {
        JDialog jDialog = new JDialog(this,true);
        switch(tabbedPaneIndex){
            case 0: //car pane
                jDialog.setTitle(texts.getString("editCar"));
                jDialog.getContentPane().add(new CarPopUp("edit",
                        ((CarTableModel)jTable1.getModel()).getSelectedCar(selectedRow),
                        texts,jTable1));
                break;
            case 1: //customer pane
                jDialog.setTitle(texts.getString("editCustomer"));
                jDialog.getContentPane().add(new CustomerPopUp("edit",
                        ((CustomerTableModel)jTable2.getModel()).getSelectedCustomer(selectedRow),
                        texts,jTable2));
                break;
            case 2: //lease pane
                jDialog.setTitle(texts.getString("editLease"));                                                
                jDialog.getContentPane().add(new LeasePopUp("edit",
                        ((LeaseTableModel)jTable3.getModel()).getSelectedLease(selectedRow),
                        texts,jTable3,dataSource));
                break;                        
        }
   
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setResizable(false);
        jDialog.setVisible(true);
    }
    
    public void newFilterJDialog(int tabbedPaneIndex){
        JDialog jDialog = new JDialog(this,true);
        switch(tabbedPaneIndex){
            case 0: //car pane
                jDialog.setTitle(texts.getString("filterCars"));
                jDialog.getContentPane().add(new CarPopUp("filter",
                        ((CarTableModel)jTable1.getModel()).getFilterCar(),
                        texts,jTable1));
                break;
            case 1: //customer pane
                jDialog.setTitle(texts.getString("filterCustomers"));
                jDialog.getContentPane().add(new CustomerPopUp("filter",
                        ((CustomerTableModel)jTable2.getModel()).getFilterCustomer(),
                        texts,jTable2));
                break;
            case 2: //lease pane
                jDialog.setTitle(texts.getString("filterLeases"));                                                
                jDialog.getContentPane().add(new LeasePopUp("filter",
                        ((LeaseTableModel)jTable3.getModel()).getFilterLease(),
                        texts,jTable3,dataSource));
                break;                        
        }
   
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setResizable(false);
        jDialog.setVisible(true);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
