package airhacks.lambda.greetings.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "base_uri")
public interface GreetingsResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hello")
    String content();

}
