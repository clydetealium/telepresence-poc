package com.tealium.poc.client.personality;

import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.tealium.poc.RequestUUIDHeaderFactory;

@Path("/personalities")
@RegisterRestClient(configKey="personality")
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
public interface PersonalityService {

    @GET
    Set<Personality> getAll();
}
