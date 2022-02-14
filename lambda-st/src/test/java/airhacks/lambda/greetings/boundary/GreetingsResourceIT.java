package airhacks.lambda.greetings.boundary;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingsResourceIT {

    @Inject
    @RestClient
    GreetingsResource resource;

    @Inject
    @ConfigProperty(name = "base_uri/mp-rest/url")
    String baseURI;

    @Test
    public void hello() {
        var message = this.resource.content();
        assertNotNull(message);
        System.out.println(message);
    }

}
