/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.ventilationserver;

/**
 *
 * @author I323506
 */
import javax.ws.rs.GET;	
import javax.ws.rs.Path;	
import javax.ws.rs.core.Response;
/**
 *
 * @author octavian
 */

@Path("/ventilation")
public class VentilationSensor{
    
    @GET	
    @Path("/state")
    public Response statet(){	
        String output = "state is ON...water is running";	
        return Response.status(200).entity(output).build();	
    }
    
    @GET	
    @Path("/start")
    public Response start(){	
        String output = "starting ventilation..";	
        return Response.status(200).entity(output).build();	
    }

    @GET	
    @Path("/stop")
    public Response stop(){	
        String output = "stop ventilation..";	
        return Response.status(200).entity(output).build();	
    }

    @GET	
    @Path("/increase-flow")
    public Response increaseFlow(){	
        String output = "increase air flow..";	
        return Response.status(200).entity(output).build();	
    }
    
    @GET	
    @Path("/decrease-flow")
    public Response decreaseFow(){	
        String output = "decreasing air flow..";	
        return Response.status(200).entity(output).build();	
    }
}
