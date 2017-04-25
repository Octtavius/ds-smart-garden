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

/**
 *
 * @author octavian
 */
@Path("/heating")
public class HeatingSensor {
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
