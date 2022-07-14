package com.tealium.poc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/localities")
public class LocalityResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String localities() {
        return "Hello from RESTEasy Reactive";
    }
}