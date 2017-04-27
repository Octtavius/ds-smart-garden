/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.heatingserver;

import	javax.ws.rs.GET;	
import	javax.ws.rs.Path;	
import	javax.ws.rs.PathParam;	
import	javax.ws.rs.core.Response;	

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author octavian
 */
@Path("/heating")
public class HeatingSensor {
    
        @GET	
    @Path("/setCurrentTemperature/{param}")
    public Response getCurrentTemperature(@PathParam("param")String state){	
        String output = "set new temp";
        
        String topic = "/sensor/heating";
        String content = state;
        int qos = 2;
        String broker = "tcp://iot.eclipse.org:1883";
        String clientId = "Publisher";
        MemoryPersistence persistence = new MemoryPersistence();
        
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
        return Response.status(200).entity(output).build();	
    }
    
        @GET	
        @Path("/state")
        public Response state(){	
            String output = "the state is OFF";	
            return Response.status(200).entity(output).build();	
        }
    
    	@GET	
        @Path("/start")
        public Response start(){	
            String output = "starting heating..";	
            return Response.status(200).entity(output).build();	
        }
        @GET	
        @Path("/stop")
        public Response stop(){	
            String output = "stop heating..";	
            return Response.status(200).entity(output).build();	
        }
        
        @GET	
        @Path("/decrease-temperature")
        public Response decreaseTemp(){	
            String output = "decreasing temperature..";	
            return Response.status(200).entity(output).build();	
        }
        
        @GET	
        @Path("/increase-temperature")
        public Response increaseTemp(){	
            String output = "increasing temperature..";	
            return Response.status(200).entity(output).build();	
        }
        
        @GET	
        @Path("/current-temperature")
        public Response getTemp(){	
            String output = "current temperature is unkown..";	
            return Response.status(200).entity(output).build();	
        }
        
//        @GET	
//        @Path("/stop")
//        public Response stop(){	
//            String output = "stop heating..";	
//            return Response.status(200).entity(output).build();	
//        }
}
