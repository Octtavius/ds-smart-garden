/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.project.ceilingsensor;


import javax.ws.rs.GET;	
import javax.ws.rs.Path;	
import javax.ws.rs.core.Response;
/**
 *
 * @author octavian
 */

@Path("/ceiling")
public class CeilingSensor{
    
    @GET	
    @Path("/state")
    public Response statet(){	
        String output = "ceiling is ...opened";	
        return Response.status(200).entity(output).build();	
    }
    
    @GET	
    @Path("/open")
    public Response open(){	
        String output = "open the ceiing..";	
        return Response.status(200).entity(output).build();	
    }

    @GET	
    @Path("/close")
    public Response close(){	
        String output = "close the ceiling..";	
        return Response.status(200).entity(output).build();	
    }
}

