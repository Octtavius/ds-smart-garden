/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.smartgarden.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author nando
 */
public class VentilationSensor {
    
    private static final String API = "/ventilation";
    private static final String API_STATE = "/state";
    private static final String API_START = "/start";
    private static final String API_STOP = "/stop";
    private static final String API_INCREASE_FLOW = "/increase-flow";
    private static final String API_DECREASE_FLOW = "/decrease-flow";
    
    private int sensor_port = 0;
    private String sensor_address = "";
    
    public VentilationSensor(String address, int port) {
        this.sensor_port = port;
        this.sensor_address = address;
        String key = address + "+" + Integer.toString(port);
        
        System.out.println("Sensor created(" + key.hashCode() + "): " + address + ":" + port);
    }
    
    public String getState() {
        makeRequest(API_STATE, "");
       return "on";
    }
    
   
    public String start() {
        String result = makeRequest(API_START, "");
        return result;
    }
    
    public String stop() {
        String result = makeRequest(API_STOP, "");
        return result;
    }
    
    public String increaseFlow() {
        String result = makeRequest(API_INCREASE_FLOW, "");
        return result;
    }
    
    public String decreaseFlow() {
        String result = makeRequest(API_DECREASE_FLOW, "");
        return result;
    }
    
    
    private String makeRequest(String endpoint, String message) {
        String output = "";
        try {

		URL url = new URL(
                        "http://"
                                + this.sensor_address
                                + ":" 
                                + this.sensor_port
                                + API 
                                + endpoint
                );
                
                System.out.println("Calling: " + url.toString());
                
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
    
}
