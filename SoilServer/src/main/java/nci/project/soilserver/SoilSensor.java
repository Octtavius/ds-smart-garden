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
import javax.ws.rs.GET;	
import javax.ws.rs.Path;	
import javax.ws.rs.core.Response;
/**
 *
 * @author octavian
 */

@Path("/soil")
public class SoilSensor{
    
    @GET	
    @Path("/measure-humidity")
    public Response measureHumidity(){	
        String output = "measure humidity";	
        return Response.status(200).entity(output).build();	
    }
    
    @GET	
    @Path("/measure-temperature")
    public Response measureTemp(){	
        String output = "measure temperature..";	
        return Response.status(200).entity(output).build();	
    }

    @GET	
    @Path("/measure-light")
    public Response measureLight(){	
        String output = "measure light..";	
        return Response.status(200).entity(output).build();	
    }

    @GET	
    @Path("/measure-nutrition")
    public Response measureNutrition(){	
        String output = "measure nutrition..";	
        return Response.status(200).entity(output).build();	
    }
}

