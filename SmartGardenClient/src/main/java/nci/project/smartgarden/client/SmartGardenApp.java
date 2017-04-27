/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.smartgarden.client;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

// mjDNS
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;

//mQTT
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author nando
 */
public class SmartGardenApp extends javax.swing.JFrame {

    private DefaultListModel sensorList = new DefaultListModel();
    private DefaultListModel logList = new DefaultListModel();
    private static AppLog logger;
    
    // Sensors
    private SoilSensor soilSensor;
    private WaterSensor waterSensor;
    private VentilationSensor ventilationSensor;
    private HeatingSensor heatingSensor;
    private CeilingSensor ceilingSensor;
    
    
    /**
     * Creates new form SmartGardenApp
     */
    public SmartGardenApp() {
        initComponents();
        
        // Set application icon
        ImageIcon icon = new ImageIcon("src/main/java/nci/project/smartgarden/client/resources/icon.png");
        setIconImage(icon.getImage());
        
        // List list to 
        jList1.setModel(sensorList);

        // Start the logger
        logger = new AppLog();
        logger.start();
        
        logger.log("Application started...");
        
        
    }

    
    private void createSensor(ServiceEvent event) {
        System.out.println("Name: " + event.getName());
        
        String address = event.getInfo().getHostAddresses()[0].trim();
        int port = event.getInfo().getPort();
        
        switch(event.getName().toString()) {
            case "soil":
                soilSensor = new SoilSensor(address, port);
                //logger.log("Soil Service state: "+ soilSensor.getState());
                toggleSoilSensorPanel();
                break;
            case "water":
                waterSensor = new WaterSensor(address, port);
                logger.log("Water Service state: "+ waterSensor.getState());
                toggleWaterSensorPanel();
                break;
            case "ventilation":
                ventilationSensor = new VentilationSensor(address, port);
                logger.log("Ventilation Service state: "+ ventilationSensor.getState());
                toggleVentilationSensor();
                break;
            case "heating":
                heatingSensor = new HeatingSensor(address, port);
                logger.log("Heating Service state: "+ heatingSensor.getState());
                toggleHeatingSensorPanel();
                break;
            case "ceiling":
                ceilingSensor = new CeilingSensor(address, port);
                logger.log("Ceiling Service state: "+ ceilingSensor.getState());
                toggleCeilingSensorPanel();
                break;
        }
    }
    
    private void destroySensor(ServiceEvent event) {
        System.out.println("Name: " + event.getName());
        
        switch(event.getName().toString()) {
            case "soil":
                soilSensor = null;
                logger.log("Soil Service removed");
                toggleSoilSensorPanel();
                break;
            case "water":
                waterSensor = null;
                logger.log("Water Service removed");
                toggleWaterSensorPanel();
                break;
            case "ventilation":
                ventilationSensor = null;
                logger.log("Ventilation Service removed");
                toggleVentilationSensor();
                break;
            case "heating":
                heatingSensor = null;
                logger.log("Heating Service state:");
                toggleHeatingSensorPanel();
                break;
            case "ceiling":
//                ceilingSensor = null;
//                logger.log("Ceiling Service removed");
//                toggleCeilingSensorPanel();
                break;
        }
    }
    
    
    private void toggleSoilSensorPanel() {
        Boolean state = lblSoilSate.getText().equals("OFF") ? true : false;
        if (state == true) {
            lblSoilSate.setText("ON");
        } else {
            lblSoilSate.setText("OFF");
        }
        for (Component cp : panelSoilSensor.getComponents() ){
            cp.setEnabled(state);
        }
    }
    
        private void toggleWaterSensorPanel() {
        if(lblWaterState.getText().equals("OFF")) {
            lblWaterState.setText("ON");
            for (Component cp : panelWaterSensor.getComponents() ){
                cp.setEnabled(true);
            }
        }
    }
        
    private void toggleHeatingSensorPanel() {
        if(lblHeatingState.getText().equals("OFF")) {
            lblHeatingState.setText("ON");
            for (Component cp : panelHeatingSensor.getComponents() ){
                cp.setEnabled(true);
            }
        }
    }

    
    private void toggleVentilationSensor() {
        if(lblVntilationState.getText().equals("OFF")) {
            lblVntilationState.setText("ON");
            for (Component cp : panelVentilationSensor.getComponents() ){
                cp.setEnabled(true);
            }
        }
    }    
 
    private void toggleCeilingSensorPanel() {
        if(lblCeilingState.getText().equals("OFF")) {
            lblCeilingState.setText("ON");
            for (Component cp : panelCeilingSensor.getComponents() ){
                cp.setEnabled(true);
            }
        }
    }
    
    private class AppLog {
        public void start() {
            listLog.setModel(logList);
        }
        
        public void log(String text) {
            logList.addElement(text);
        }
    }
    
    private class SampleListener implements ServiceListener {
            @Override
            public void serviceAdded(ServiceEvent event) {
                    System.out.println("Service added: " + event.getInfo());
            }

            @Override
            public void serviceRemoved(ServiceEvent event) {
                    System.out.println("Service removed: " + event.getInfo());
                    destroySensor(event);
            }

            @Override
            public void serviceResolved(ServiceEvent event) {
                System.out.println("Service resolved: " + event.getInfo());
                ServiceInfo info = event.getInfo();
                int port = info.getPort();
                String path = info.getNiceTextString().split("=")[1];
                //GetRequest.request("http://localhost:"+port+"/"+path);
                sensorList.addElement(info.getName() + " : " + info.getPort());
                logger.log("Service found: " + info.getHostAddresses()[0] + " " + info.getPort());
                createSensor(event);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        scrollPanelLog = new javax.swing.JScrollPane();
        listLog = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        panelSoilSensor = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblSoilSate = new java.awt.Label();
        jLabel13 = new javax.swing.JLabel();
        lblHumidity = new javax.swing.JLabel();
        btnReadHumidity = new javax.swing.JButton();
        btnReadTemperature = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        lblTemperature = new javax.swing.JLabel();
        btnReadLight = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        lblLight = new javax.swing.JLabel();
        btnReadNutrition = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        lblNutrition = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        panelWaterSensor = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblWaterState = new java.awt.Label();
        btnWaterStop = new javax.swing.JButton();
        btnDecreaseWater = new javax.swing.JButton();
        btnReadCurrentWater = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        btnIncreaseWater = new javax.swing.JButton();
        btnWaterStart = new javax.swing.JButton();
        lblCurrentWater = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        panelHeatingSensor = new javax.swing.JPanel();
        lblHeatingIcon = new javax.swing.JLabel();
        lblHeatingState = new java.awt.Label();
        btnHeatingStop = new javax.swing.JButton();
        btnHeatingStart = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnIncreaseHeating = new javax.swing.JButton();
        btnDecreaseHeating = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        lblCurrentTemperature = new javax.swing.JLabel();
        btnReadCurrentTemp = new javax.swing.JButton();
        panelVentilationSensor = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblVntilationState = new java.awt.Label();
        btnVentilationStop = new javax.swing.JButton();
        btnVentilationStart = new javax.swing.JButton();
        btnIncreaseVentilation = new javax.swing.JButton();
        btnDecreaseVentialtion = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        panelCeilingSensor = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblCeilingState = new java.awt.Label();
        btnCeilingState = new javax.swing.JButton();
        btnCeilingOpen = new javax.swing.JButton();
        btnCeilingClose = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        lblCeilingSate = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Sensor 1", "Sensor 2", " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        listLog.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        scrollPanelLog.setViewportView(listLog);

        jButton1.setText("Scan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Subscribe");
        jButton2.setActionCommand("Start Subscriber");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPanelLog)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPanelLog, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelSoilSensor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setIcon( new ImageIcon("src/main/java/nci/project/smartgarden/client/resources/soil_icon.png"));

        lblSoilSate.setText("OFF");

        jLabel13.setText("Humidity ");

        lblHumidity.setText("0");

        btnReadHumidity.setText("Humidity");
        btnReadHumidity.setEnabled(false);
        btnReadHumidity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadHumidityActionPerformed(evt);
            }
        });

        btnReadTemperature.setText("Temperature");
        btnReadTemperature.setEnabled(false);
        btnReadTemperature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadTemperatureActionPerformed(evt);
            }
        });

        jLabel14.setText("Temeprature:");

        lblTemperature.setText("0");

        btnReadLight.setText("Light");
        btnReadLight.setEnabled(false);
        btnReadLight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadLightActionPerformed(evt);
            }
        });

        jLabel15.setText("Light");

        lblLight.setText("0");

        btnReadNutrition.setText("Nutrition");
        btnReadNutrition.setEnabled(false);
        btnReadNutrition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadNutritionActionPerformed(evt);
            }
        });

        jLabel16.setText("Nutrition");

        lblNutrition.setText("0");

        jLabel17.setText("Read the current values");

        javax.swing.GroupLayout panelSoilSensorLayout = new javax.swing.GroupLayout(panelSoilSensor);
        panelSoilSensor.setLayout(panelSoilSensorLayout);
        panelSoilSensorLayout.setHorizontalGroup(
            panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSoilSensorLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addGroup(panelSoilSensorLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReadHumidity)
                            .addGroup(panelSoilSensorLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHumidity)))
                        .addGap(18, 18, 18)
                        .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReadTemperature)
                            .addGroup(panelSoilSensorLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemperature)))
                        .addGap(18, 18, 18)
                        .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReadLight)
                            .addGroup(panelSoilSensorLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblLight)))))
                .addGap(27, 27, 27)
                .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReadNutrition)
                    .addGroup(panelSoilSensorLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNutrition)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSoilSate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelSoilSensorLayout.setVerticalGroup(
            panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSoilSensorLayout.createSequentialGroup()
                .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelSoilSensorLayout.createSequentialGroup()
                        .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(lblTemperature))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReadTemperature))
                    .addGroup(panelSoilSensorLayout.createSequentialGroup()
                        .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(lblHumidity))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReadHumidity))
                    .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelSoilSensorLayout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelSoilSensorLayout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(lblSoilSate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelSoilSensorLayout.createSequentialGroup()
                            .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(lblNutrition))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnReadNutrition))
                        .addGroup(panelSoilSensorLayout.createSequentialGroup()
                            .addGroup(panelSoilSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(lblLight))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnReadLight))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        lblSoilSate.getAccessibleContext().setAccessibleName("ON");

        panelWaterSensor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setIcon( new ImageIcon("src/main/java/nci/project/smartgarden/client/resources/water_icon.png"));

        lblWaterState.setText("OFF");

        btnWaterStop.setText("Stop");
        btnWaterStop.setEnabled(false);
        btnWaterStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWaterStopActionPerformed(evt);
            }
        });

        btnDecreaseWater.setEnabled(false);
        btnDecreaseWater.setLabel("-");
        btnDecreaseWater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecreaseWaterActionPerformed(evt);
            }
        });

        btnReadCurrentWater.setText("Read Water Flow");
        btnReadCurrentWater.setEnabled(false);
        btnReadCurrentWater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadCurrentWaterActionPerformed(evt);
            }
        });

        jLabel11.setText("Change Water Flow:");

        btnIncreaseWater.setEnabled(false);
        btnIncreaseWater.setLabel("+");
        btnIncreaseWater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncreaseWaterActionPerformed(evt);
            }
        });

        btnWaterStart.setText("Start");
        btnWaterStart.setEnabled(false);
        btnWaterStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWaterStartActionPerformed(evt);
            }
        });

        lblCurrentWater.setText("0");

        jLabel12.setText("Currrent Flow: ");

        javax.swing.GroupLayout panelWaterSensorLayout = new javax.swing.GroupLayout(panelWaterSensor);
        panelWaterSensor.setLayout(panelWaterSensorLayout);
        panelWaterSensorLayout.setHorizontalGroup(
            panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelWaterSensorLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnWaterStop, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelWaterSensorLayout.createSequentialGroup()
                        .addComponent(btnWaterStart)
                        .addGap(1, 1, 1)))
                .addGap(40, 40, 40)
                .addComponent(jLabel11)
                .addGap(13, 13, 13)
                .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDecreaseWater, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnIncreaseWater, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelWaterSensorLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnReadCurrentWater))
                    .addGroup(panelWaterSensorLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCurrentWater)))
                .addGap(17, 17, 17)
                .addComponent(lblWaterState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelWaterSensorLayout.setVerticalGroup(
            panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelWaterSensorLayout.createSequentialGroup()
                .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelWaterSensorLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelWaterSensorLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblWaterState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelWaterSensorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelWaterSensorLayout.createSequentialGroup()
                                .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(lblCurrentWater))
                                    .addComponent(btnIncreaseWater))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelWaterSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnDecreaseWater)
                                    .addComponent(btnReadCurrentWater)))
                            .addGroup(panelWaterSensorLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel11)))))
                .addContainerGap(11, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelWaterSensorLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnWaterStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnWaterStop)
                .addGap(10, 10, 10))
        );

        panelHeatingSensor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblHeatingIcon.setIcon( new ImageIcon("src/main/java/nci/project/smartgarden/client/resources/temp_icon.png"));

        lblHeatingState.setText("OFF");

        btnHeatingStop.setText("Stop");
        btnHeatingStop.setEnabled(false);
        btnHeatingStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHeatingStopActionPerformed(evt);
            }
        });

        btnHeatingStart.setText("Start");
        btnHeatingStart.setEnabled(false);
        btnHeatingStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHeatingStartActionPerformed(evt);
            }
        });

        jLabel7.setText("Change Temperature:");

        btnIncreaseHeating.setEnabled(false);
        btnIncreaseHeating.setLabel("+");
        btnIncreaseHeating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncreaseHeatingActionPerformed(evt);
            }
        });

        btnDecreaseHeating.setEnabled(false);
        btnDecreaseHeating.setLabel("-");
        btnDecreaseHeating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecreaseHeatingActionPerformed(evt);
            }
        });

        jLabel8.setText("Currrent Temp: ");

        lblCurrentTemperature.setText("0");

        btnReadCurrentTemp.setText("Read Temperature");
        btnReadCurrentTemp.setEnabled(false);
        btnReadCurrentTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadCurrentTempActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelHeatingSensorLayout = new javax.swing.GroupLayout(panelHeatingSensor);
        panelHeatingSensor.setLayout(panelHeatingSensorLayout);
        panelHeatingSensorLayout.setHorizontalGroup(
            panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblHeatingIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHeatingStop, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeatingSensorLayout.createSequentialGroup()
                        .addComponent(btnHeatingStart)
                        .addGap(1, 1, 1)))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(13, 13, 13)
                .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDecreaseHeating, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnIncreaseHeating, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnReadCurrentTemp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(lblHeatingState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCurrentTemperature)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelHeatingSensorLayout.setVerticalGroup(
            panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblHeatingIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblHeatingState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeatingSensorLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                        .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(lblCurrentTemperature))
                            .addComponent(btnIncreaseHeating))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelHeatingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDecreaseHeating)
                            .addComponent(btnReadCurrentTemp)))
                    .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel7))
                    .addGroup(panelHeatingSensorLayout.createSequentialGroup()
                        .addComponent(btnHeatingStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHeatingStop)))
                .addGap(12, 12, 12))
        );

        panelVentilationSensor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setBackground(new java.awt.Color(102, 102, 102));
        jLabel5.setIcon( new ImageIcon("src/main/java/nci/project/smartgarden/client/resources/wind_icon.png"));

        lblVntilationState.setText("OFF");

        btnVentilationStop.setText("Stop");
        btnVentilationStop.setEnabled(false);
        btnVentilationStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentilationStopActionPerformed(evt);
            }
        });

        btnVentilationStart.setText("Start");
        btnVentilationStart.setEnabled(false);
        btnVentilationStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentilationStartActionPerformed(evt);
            }
        });

        btnIncreaseVentilation.setEnabled(false);
        btnIncreaseVentilation.setLabel("+");
        btnIncreaseVentilation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncreaseVentilationActionPerformed(evt);
            }
        });

        btnDecreaseVentialtion.setEnabled(false);
        btnDecreaseVentialtion.setLabel("-");
        btnDecreaseVentialtion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecreaseVentialtionActionPerformed(evt);
            }
        });

        jLabel6.setText("Change Air Flow:");

        javax.swing.GroupLayout panelVentilationSensorLayout = new javax.swing.GroupLayout(panelVentilationSensor);
        panelVentilationSensor.setLayout(panelVentilationSensorLayout);
        panelVentilationSensorLayout.setHorizontalGroup(
            panelVentilationSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVentilationSensorLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(panelVentilationSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVentilationSensorLayout.createSequentialGroup()
                        .addComponent(btnVentilationStop)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVentilationSensorLayout.createSequentialGroup()
                        .addComponent(btnVentilationStart)
                        .addGap(50, 50, 50)))
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelVentilationSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDecreaseVentialtion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnIncreaseVentilation, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblVntilationState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelVentilationSensorLayout.setVerticalGroup(
            panelVentilationSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVentilationSensorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblVntilationState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
            .addGroup(panelVentilationSensorLayout.createSequentialGroup()
                .addGroup(panelVentilationSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVentilationSensorLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelVentilationSensorLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panelVentilationSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVentilationStart)
                            .addComponent(btnIncreaseVentilation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelVentilationSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVentilationStop)
                            .addComponent(btnDecreaseVentialtion)))
                    .addGroup(panelVentilationSensorLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel6)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelCeilingSensor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setIcon( new ImageIcon("src/main/java/nci/project/smartgarden/client/resources/ceilling_icon.png"));

        lblCeilingState.setText("OFF");

        btnCeilingState.setText("Get State");
        btnCeilingState.setEnabled(false);
        btnCeilingState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCeilingStateActionPerformed(evt);
            }
        });

        btnCeilingOpen.setText("Open");
        btnCeilingOpen.setEnabled(false);
        btnCeilingOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCeilingOpenActionPerformed(evt);
            }
        });

        btnCeilingClose.setText("Close");
        btnCeilingClose.setEnabled(false);
        btnCeilingClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCeilingCloseActionPerformed(evt);
            }
        });

        jLabel9.setText("Current State:");

        lblCeilingSate.setText("Open");

        javax.swing.GroupLayout panelCeilingSensorLayout = new javax.swing.GroupLayout(panelCeilingSensor);
        panelCeilingSensor.setLayout(panelCeilingSensorLayout);
        panelCeilingSensorLayout.setHorizontalGroup(
            panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCeilingSate))
                    .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(btnCeilingState)))
                .addGap(105, 105, 105)
                .addGroup(panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                        .addComponent(btnCeilingOpen)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                        .addComponent(btnCeilingClose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCeilingState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelCeilingSensorLayout.setVerticalGroup(
            panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCeilingSensorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCeilingOpen)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(lblCeilingSate)))
                .addGroup(panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCeilingState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(panelCeilingSensorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelCeilingSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCeilingClose)
                            .addComponent(btnCeilingState))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelSoilSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelWaterSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelHeatingSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelVentilationSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelCeilingSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelSoilSensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelWaterSensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelHeatingSensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelVentilationSensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelCeilingSensor, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        logger.log("Discovering sensors...");
        
        class SampleSubscriber implements MqttCallback {

            public SampleSubscriber() {
            }

            @Override
            public void connectionLost(Throwable thrwbl) {
            }

            @Override
            public void messageArrived(String string, MqttMessage mm) throws Exception {
                System.out.println(mm + " arrived from topic " + string);
                if(string.toLowerCase().contains("soil")){
                    System.out.println("message came from soil sensor");
                    if(string.toLowerCase().contains("humidity")){
//                        System.out.println("humidity update");
                        lblCurrentTemperature1.setText(mm+"");
                    }
                    else if(string.toLowerCase().contains("temperature")){
                        lblCurrentTemperature2.setText(mm+"");
//                        System.out.println("temperature update");
                    }
                    else if(string.toLowerCase().contains("light")){
                        lblCurrentTemperature3.setText(mm+"");
//                        System.out.println("light update");
                    }
                    else if(string.toLowerCase().contains("nutrition")){
                        lblCurrentTemperature4.setText(mm+"");
//                        System.out.println("nutrition update");
                    }
                }
                else if(string.toLowerCase().contains("ceiling")){
                    System.out.println("message came from ceiling sensor");
                    lblCeilingState.setText(mm + "");
                }
                else if(string.toLowerCase().contains("heating")){
                    System.out.println("message came from heating sensor");
                    lblCurrentTemperature.setText(mm + "");
                }
                else if(string.toLowerCase().contains("water")){
                    System.out.println("message came from water sensor");
                    lblCurrentWater.setText(mm + "");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken imdt) {
            }

        }
        
        try {
            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            // Add a service listener
            jmdns.addServiceListener("_nando._tcp.local.", new SampleListener());
            jmdns.addServiceListener("_water._tcp.local.", new SampleListener());
            jmdns.addServiceListener("_heating._tcp.local.", new SampleListener());
            jmdns.addServiceListener("_ceiling._tcp.local.", new SampleListener());
            jmdns.addServiceListener("_soil._tcp.local.", new SampleListener());
            jmdns.addServiceListener("_ventilation._tcp.local.", new SampleListener());
            
            System.out.println("Service listener added...");
            
            // Wait a bit
            Thread.sleep(2500);
            
            String broker = "tcp://iot.eclipse.org:1883";
            String clientId = "Subscriber";
            MemoryPersistence persistence = new MemoryPersistence();

            try {
                MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                sampleClient.setCallback(new SampleSubscriber());
                System.out.println("Connecting to broker: " + broker);
                sampleClient.connect(connOpts);
                System.out.println("Connected");
                
                
                sampleClient.subscribe("/sensor/ceiling/#");
                sampleClient.subscribe("/sensor/soil/#");
                sampleClient.subscribe("/sensor/heating/#");
                sampleClient.subscribe("/sensor/water/#");
                
                
            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            }

        } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
        } catch (IOException e) {
                System.out.println(e.getMessage());
        } catch (InterruptedException ex) {
            Logger.getLogger(SmartGardenApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            int index = list.locationToIndex(evt.getPoint());
            System.out.println("index: "+index);
        }
    }//GEN-LAST:event_jList1MouseClicked


    String topic = "/smartgarden/sensors";
    String content = "Message from MqttPublishSample";
    int qos = 2;
    String broker = "tcp://iot.eclipse.org:1883";
    String clientId = "Publisher";
    MemoryPersistence persistence = new MemoryPersistence();
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Start mQTT Subscriber Service
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnVentilationStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentilationStartActionPerformed
        logger.log(ventilationSensor.start());
    }//GEN-LAST:event_btnVentilationStartActionPerformed

    private void btnVentilationStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentilationStopActionPerformed
       logger.log(ventilationSensor.stop());
    }//GEN-LAST:event_btnVentilationStopActionPerformed

    private void btnIncreaseVentilationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncreaseVentilationActionPerformed
        logger.log(ventilationSensor.increaseFlow());
    }//GEN-LAST:event_btnIncreaseVentilationActionPerformed

    private void btnDecreaseVentialtionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecreaseVentialtionActionPerformed
        logger.log(ventilationSensor.decreaseFlow());
    }//GEN-LAST:event_btnDecreaseVentialtionActionPerformed

    private void btnCeilingOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCeilingOpenActionPerformed
        logger.log(ceilingSensor.open());
    }//GEN-LAST:event_btnCeilingOpenActionPerformed

    private void btnCeilingCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCeilingCloseActionPerformed
        logger.log(ceilingSensor.close());
    }//GEN-LAST:event_btnCeilingCloseActionPerformed

    private void btnWaterStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWaterStartActionPerformed
        logger.log(waterSensor.start());
    }//GEN-LAST:event_btnWaterStartActionPerformed

    private void btnWaterStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWaterStopActionPerformed
        logger.log(waterSensor.stop());
    }//GEN-LAST:event_btnWaterStopActionPerformed

    private void btnIncreaseWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncreaseWaterActionPerformed
        logger.log(waterSensor.increaseFlow());
    }//GEN-LAST:event_btnIncreaseWaterActionPerformed

    private void btnDecreaseWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecreaseWaterActionPerformed
        logger.log(waterSensor.decreaseFlow());
    }//GEN-LAST:event_btnDecreaseWaterActionPerformed

    private void btnReadCurrentWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadCurrentWaterActionPerformed
        String water = waterSensor.getState();
        logger.log("Current water flow: " + water);
        lblCurrentWater.setText(water);
    }//GEN-LAST:event_btnReadCurrentWaterActionPerformed

    private void btnReadHumidityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadHumidityActionPerformed
        String humidity = soilSensor.measureHumidity();
        logger.log("Soil Humidity: " + humidity);
        
    }//GEN-LAST:event_btnReadHumidityActionPerformed

    private void btnReadTemperatureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadTemperatureActionPerformed
        String temp = soilSensor.measureTemperature();
        logger.log("Current soild temperature: " + temp);
        lblTemperature.setText(temp);
    }//GEN-LAST:event_btnReadTemperatureActionPerformed

    private void btnReadLightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadLightActionPerformed
        String light = soilSensor.measureLight();
        logger.log("Current soild light: " + light);
        lblLight.setText(light);
    }//GEN-LAST:event_btnReadLightActionPerformed

    private void btnReadNutritionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadNutritionActionPerformed
        String nutrition = soilSensor.measureNutrition();
        logger.log("Current soild nutrition: " + nutrition);
        lblNutrition.setText(nutrition);
    }//GEN-LAST:event_btnReadNutritionActionPerformed

    private void btnReadCurrentTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadCurrentTempActionPerformed
        String temp = heatingSensor.getCurrentTemp();
        logger.log("Current temp: " + temp);
        lblCurrentTemperature.setText(temp);
    }//GEN-LAST:event_btnReadCurrentTempActionPerformed

    private void btnDecreaseHeatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecreaseHeatingActionPerformed
        logger.log(heatingSensor.decreaseTemp());
    }//GEN-LAST:event_btnDecreaseHeatingActionPerformed

    private void btnIncreaseHeatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncreaseHeatingActionPerformed
        logger.log(heatingSensor.increaseTemp());
    }//GEN-LAST:event_btnIncreaseHeatingActionPerformed

    private void btnHeatingStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHeatingStartActionPerformed
        logger.log(heatingSensor.start());
    }//GEN-LAST:event_btnHeatingStartActionPerformed

    private void btnHeatingStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHeatingStopActionPerformed
        logger.log(heatingSensor.stop());
    }//GEN-LAST:event_btnHeatingStopActionPerformed

    private void btnCeilingStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCeilingStateActionPerformed
        String state = ceilingSensor.getState();
        logger.log("Current ceiling state: " + state);
        lblCeilingSate.setText(state);
        
    }//GEN-LAST:event_btnCeilingStateActionPerformed

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
            java.util.logging.Logger.getLogger(SmartGardenApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SmartGardenApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SmartGardenApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SmartGardenApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SmartGardenApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton btnCeilingClose;
    private javax.swing.JButton btnCeilingOpen;
    private javax.swing.JButton btnCeilingState;
    private javax.swing.JButton btnDecreaseHeating;
    private javax.swing.JButton btnDecreaseVentialtion;
    private javax.swing.JButton btnDecreaseWater;
    private javax.swing.JButton btnHeatingStart;
    private javax.swing.JButton btnHeatingStop;
    private javax.swing.JButton btnIncreaseHeating;
    private javax.swing.JButton btnIncreaseVentilation;
    private javax.swing.JButton btnIncreaseWater;
    private javax.swing.JButton btnReadCurrentTemp;
    private javax.swing.JButton btnReadCurrentWater;
    private javax.swing.JButton btnReadHumidity;
    private javax.swing.JButton btnReadLight;
    private javax.swing.JButton btnReadNutrition;
    private javax.swing.JButton btnReadTemperature;
    private javax.swing.JButton btnVentilationStart;
    private javax.swing.JButton btnVentilationStop;
    private javax.swing.JButton btnWaterStart;
    private javax.swing.JButton btnWaterStop;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCeilingSate;
    private java.awt.Label lblCeilingState;
    private javax.swing.JLabel lblCurrentTemperature;
    private javax.swing.JLabel lblCurrentWater;
    private javax.swing.JLabel lblHeatingIcon;
    private java.awt.Label lblHeatingState;
    private javax.swing.JLabel lblHumidity;
    private javax.swing.JLabel lblLight;
    private javax.swing.JLabel lblNutrition;
    private java.awt.Label lblSoilSate;
    private javax.swing.JLabel lblTemperature;
    private java.awt.Label lblVntilationState;
    private java.awt.Label lblWaterState;
    private javax.swing.JList<String> listLog;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel panelCeilingSensor;
    private javax.swing.JPanel panelHeatingSensor;
    private javax.swing.JPanel panelSoilSensor;
    private javax.swing.JPanel panelVentilationSensor;
    private javax.swing.JPanel panelWaterSensor;
    private javax.swing.JScrollPane scrollPanelLog;
    // End of variables declaration//GEN-END:variables

}
