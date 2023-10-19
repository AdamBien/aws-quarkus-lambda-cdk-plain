package airhacks.lambda.time.boundary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("time")
@ApplicationScoped
public class CurrentTimeResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String currentTime(){
        return LocalDateTime
        .now()
        .format(DateTimeFormatter.ofPattern("d MMM uuuu, HH:mm:ss"));
        
    }
    
}
