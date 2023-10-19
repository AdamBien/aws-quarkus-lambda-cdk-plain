package airhacks.lambda.greetings.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "base_uri")
public interface GreetingsResourceClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hello")
    String content();

}
