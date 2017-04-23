/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.smartgarden;

import javax.ws.rs.GET;	
import javax.ws.rs.Path;	
import javax.ws.rs.core.Response;
/**
 *
 * @author octavian
 */

@Path("/init")
public class WaterSensor{
    
/**
 *
 * @author octavian
 */
public class ServiceRegistration {
    public ServiceRegistration() {
//        try {
//            //create aJmDNS instance
//            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
//            
//            //Register  a service
//            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "example", 1234, "path=index.html");
//            jmdns.registerService(serviceInfo);
//            
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(ServiceRegistration.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ServiceRegistration.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
    
    @GET
    public Response sayHelloWorld()	{
        String output = "Hello Jim  >  !";
//        ServiceRegistration sr = new ServiceRegistration();
        return Response.status(200).entity(output).build();
    }	
}
