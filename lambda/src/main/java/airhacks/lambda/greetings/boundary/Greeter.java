package airhacks.lambda.greetings.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Greeter {

    @Inject
    @ConfigProperty(defaultValue = "hello, quarkus on AWS", name="message")
    String message;
    
    public String greetings() {
        return this.message;
    }

    public void greetings(String message) {
        System.out.println("received: " + message);
    }
}
