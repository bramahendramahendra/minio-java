package org.acme;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
// @RolesAllowed("bob")
public class GreetingResource {
    // @Inject CounterBean counter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    // @RolesAllowed("bob")
    public String hello() {

        return "Hello from Quarkus REST";
    }

    
}
