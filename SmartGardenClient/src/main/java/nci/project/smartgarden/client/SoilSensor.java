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
public class SoilSensor {
        
    private static final String API = "/soil";
    private static final String API_MEASURE_HUMIDITY = "/measure-humidity";
    private static final String API_MEASURE_TEMPERATURE = "/measure-temperature";
    private static final String API_MEASURE_LIGHT = "/measure-light";
    private static final String API_MEASURE_NUTRITION = "/measure-nutrition";
    
    private int sensor_port = 0;
    private String sensor_address = "";
    
    public SoilSensor(String address, int port) {
        this.sensor_port = port;
        this.sensor_address = address;
        String key = address + "+" + Integer.toString(port);
        
        System.out.println("Sensor created(" + key.hashCode() + "): " + address + ":" + port);
    }
    
    public String measureHumidity() {
        makeRequest(API_MEASURE_HUMIDITY, "");
       return "on";
    }
    
   
    public String measureTemperature() {
        String result = makeRequest(API_MEASURE_TEMPERATURE, "");
        return result;
    }
    
    public String measureLight() {
        String result = makeRequest(API_MEASURE_LIGHT, "");
        return result;
    }
    
    public String measureNutrition() {
        String result = makeRequest(API_MEASURE_NUTRITION, "");
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
