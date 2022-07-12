package com.tealium.poc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/personalities")
public class PersonalityResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String personalities() {
        return "Hello from RESTEasy Reactive";
    }
}