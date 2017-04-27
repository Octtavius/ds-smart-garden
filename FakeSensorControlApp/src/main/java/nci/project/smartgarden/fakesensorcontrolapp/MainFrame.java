/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.smartgarden.fakesensorcontrolapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author fmorais
 */
public class MainFrame extends javax.swing.JFrame {

    private static final String SENSOR_API = "/soil";
    private static final String SENSOR_PORT = "3003";
    private static final String SENSOR_ADDRESS = "127.0.01";
    private static final String SOIL_SET_LIGHT = "/set-new-light/";
    private static final String SOIL_SET_HUMIDITY = "/set-new-humidity/";
    private static final String SOIL_SET_TEMPERATURE = "/set-new-temperature/";
    private static final String SOIL_SET_NUTRITION = "/set-new-nutrition/";

    private String makeRequest(String endpoint, String value) {
        String output = "";
        try {

		URL url = new URL(
                        "http://"
                                + SENSOR_ADDRESS
                                + ":" 
                                + SENSOR_PORT
                                + SENSOR_API 
                                + endpoint
                                + value
                );
                
                System.out.println("Soil Sensor: " + url.toString());
                
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		System.out.println("Output from Server.... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();

	  } catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  }
          
        return output;
    }
    
    /** Creates new form NewJFrame */
    public MainFrame() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnUpdateHumidity = new javax.swing.JToggleButton();
        btnUpdateTemeprature = new javax.swing.JToggleButton();
        btnUpdateNutrition = new javax.swing.JToggleButton();
        btnUpdateLight = new javax.swing.JToggleButton();
        txtTempLevel = new javax.swing.JTextField();
        txtHumidityLevel = new javax.swing.JTextField();
        txtNutritionLevel = new javax.swing.JTextField();
        comboLightLevel = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Humidity Level:");

        jLabel2.setText("Temperature Level:");

        jLabel3.setText("Light Level:");

        jLabel4.setText("Nutrition Level:");

        btnUpdateHumidity.setText("Update");
        btnUpdateHumidity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateHumidityActionPerformed(evt);
            }
        });

        btnUpdateTemeprature.setText("Update");
        btnUpdateTemeprature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTemepratureActionPerformed(evt);
            }
        });

        btnUpdateNutrition.setText("Update");
        btnUpdateNutrition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateNutritionActionPerformed(evt);
            }
        });

        btnUpdateLight.setText("Update");
        btnUpdateLight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateLightActionPerformed(evt);
            }
        });

        txtTempLevel.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        txtHumidityLevel.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        txtNutritionLevel.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        comboLightLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dark", "Dim", "Bright" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboLightLevel, 0, 112, Short.MAX_VALUE)
                    .addComponent(txtHumidityLevel)
                    .addComponent(txtTempLevel)
                    .addComponent(txtNutritionLevel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUpdateNutrition)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnUpdateHumidity)
                        .addComponent(btnUpdateTemeprature)
                        .addComponent(btnUpdateLight, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnUpdateHumidity)
                    .addComponent(txtHumidityLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnUpdateTemeprature)
                    .addComponent(txtTempLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(btnUpdateLight)
                    .addComponent(comboLightLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnUpdateNutrition)
                    .addComponent(txtNutritionLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateHumidityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateHumidityActionPerformed
        makeRequest(SOIL_SET_HUMIDITY, txtHumidityLevel.getText());
    }//GEN-LAST:event_btnUpdateHumidityActionPerformed

    private void btnUpdateTemepratureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateTemepratureActionPerformed
        makeRequest(SOIL_SET_TEMPERATURE, txtTempLevel.getText());
    }//GEN-LAST:event_btnUpdateTemepratureActionPerformed

    private void btnUpdateNutritionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateNutritionActionPerformed
        makeRequest(SOIL_SET_NUTRITION, txtNutritionLevel.getText());
    }//GEN-LAST:event_btnUpdateNutritionActionPerformed

    private void btnUpdateLightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateLightActionPerformed
        makeRequest(SOIL_SET_LIGHT, comboLightLevel.getSelectedItem().toString());
    }//GEN-LAST:event_btnUpdateLightActionPerformed

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnUpdateHumidity;
    private javax.swing.JToggleButton btnUpdateLight;
    private javax.swing.JToggleButton btnUpdateNutrition;
    private javax.swing.JToggleButton btnUpdateTemeprature;
    private javax.swing.JComboBox<String> comboLightLevel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtHumidityLevel;
    private javax.swing.JTextField txtNutritionLevel;
    private javax.swing.JTextField txtTempLevel;
    // End of variables declaration//GEN-END:variables

    
}
