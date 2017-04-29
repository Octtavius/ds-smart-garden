/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.soilserver;

/**
 *
 * @author I323506
 */
import com.google.gson.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;	
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;	
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author octavian
 */

@Path("/soil")
public class SoilSensor{
    
    public static class JsonResponse{
        private String username;
        
        public JsonResponse() {
            
        }
        
        public JsonResponse(String u) {
            this.username = u;
        }
        
        public void setU(String u) {
            this.username = u;
        }
        
        public String getU() {
            return username;
        }
        
    @Override
    public String toString() {
        return new StringBuffer(" First Name : ").append(this.username).toString();
    }

        
        
    }
    
    private String topic = null;
    private String output = null;
    private String content = null;
    private final String broker = "tcp://iot.eclipse.org:1883";
//    private
    private final int qos = 2;
    
    @POST
    @Path("/action")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes(MediaType.APPLICATION_JSON)
    public void doAction(JsonResponse jObj) {
        //create  acustomer object. REMEMBER: the id is not set yet.
        //it will be generated in the right class. not here. 
        
//        String action = formParams.getFirst("username");
//        String value = formParams.getFirst("password");       
        System.out.println(jObj.getU());
//        JsonObject jObj = new JsonObject(); 
        
        
//        Customer tempCustomer = createCustomerObject(formParams);
//        
//        int result = -1;
//        result = rep.createAccount(tempCustomer, accountType, password);
//        if(result != -1){
//            jObj.addProperty("response", result);            
//            return Response.status(200).entity(jObj.toString()).build();
//            
//        }
//        else {
//            return Response.status(400).build(); 
//        }

    }
    
    @GET	
    @Path("/set-new-humidity/{param}")
    public Response setNewHumidity(@PathParam("param")String state){	
        output = "set new humidity";
        
        topic = "/sensor/soil/humidity";
        content = state;
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
    @Path("/set-new-temperature/{param}")
    public Response setNewTemperature(@PathParam("param")String state){	
        output = "set new temperature";
        
        topic = "/sensor/soil/temperature";
        content = state;
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
    @Path("/set-new-light/{param}")
    public Response setNewLight(@PathParam("param")String state){	
        output = "set new light";
        
        topic = "/sensor/soil/light";
        content = state;
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
    @Path("/set-new-nutrition/{param}")
    public Response setNewNutrition(@PathParam("param")String state){	
        output = "set new nutrition";
        
        topic = "/sensor/soil/nutrition";
        content = state;
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
    @Path("/measure-humidity")
    public Response measureHumidity(){	
        output = "measure humidity";	
        return Response.status(200).entity(output).build();	
    }
    
    @GET	
    @Path("/measure-temperature")
    public Response measureTemp(){	
        output = "measure temperature..";	
        return Response.status(200).entity(output).build();	
    }

    @GET	
    @Path("/measure-light")
    public Response measureLight(){	
        output = "measure light..";	
        return Response.status(200).entity(output).build();	
    }

    @GET	
    @Path("/measure-nutrition")
    public Response measureNutrition(){	
        output = "measure nutrition..";	
        return Response.status(200).entity(output).build();	
    }
}

