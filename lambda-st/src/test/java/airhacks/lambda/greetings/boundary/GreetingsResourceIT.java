package airhacks.lambda.greetings.boundary;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class GreetingsResourceIT {

    @Inject
    @RestClient
    GreetingsResourceClient rut;

    @Inject
    @ConfigProperty(name = "base_uri/mp-rest/url")
    String baseURI;

    @Test
    public void hello() {
        var message = this.rut.content();
        assertNotNull(message);
        System.out.println(message);
    }

}
