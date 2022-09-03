package com.tealium.poc.client.locality;

import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.tealium.poc.RequestUUIDHeaderFactory;

@Path("/localities")
@RegisterRestClient(configKey="locality")
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
public interface LocalityService {

    @GET
    Set<Locality> getAll();
}
