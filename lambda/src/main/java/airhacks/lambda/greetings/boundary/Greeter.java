package airhacks.lambda.greetings.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Greeter {

    static System.Logger LOG = System.getLogger(Greeter.class.getName()); 

    @Inject
    @ConfigProperty(defaultValue = "hello, quarkus on AWS", name="message")
    String message;
    
    public String greetings() {
        return this.message;
    }

    public void greetings(String message) {
        LOG.log(System.Logger.Level.INFO, "received: " + message);
    }
}
