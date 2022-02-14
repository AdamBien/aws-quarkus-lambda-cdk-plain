package airhacks.lambda.greetings.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "base_uri")
public interface GreetingsResource {

    @GET
    @Path("hello")
    String content();

}
