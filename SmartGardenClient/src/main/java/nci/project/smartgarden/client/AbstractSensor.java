/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.smartgarden.client;

/**
 *
 * @author Nando
 */
public abstract class AbstractSensor {

    private int sensor_port = 0;
    private String sensor_address = "";
    
    public AbstractSensor(String address, int port) {
        this.sensor_port = port;
        this.sensor_address = address;
        String key = address + "+" + Integer.toString(port);
        
        System.out.println("Sensor created(" + key.hashCode() + "): " + address + ":" + port);
    }
    
    
    public abstract String getState();
    
    
    /**
     * @return the sensor_port
     */
    public int getSensor_port() {
        return sensor_port;
    }

    /**
     * @param sensor_port the sensor_port to set
     */
    public void setSensor_port(int sensor_port) {
        this.sensor_port = sensor_port;
    }

    /**
     * @return the sensor_address
     */
    public String getSensor_address() {
        return sensor_address;
    }

    /**
     * @param sensor_address the sensor_address to set
     */
    public void setSensor_address(String sensor_address) {
        this.sensor_address = sensor_address;
    }
       
    
}
