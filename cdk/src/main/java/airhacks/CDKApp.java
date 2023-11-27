package airhacks;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;

public interface CDKApp {

    static void main(String... args) {

        var app = new App();
        var appName = "quarkus-lambda";

        Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda");
        Tags.of(app).add("environment", "development");
        Tags.of(app).add("application", appName);

        var stack = new InfrastructureBuilder(app, appName)
                .functionName("airhacks_QuarkusOnLambda")
                .functionURLBuilder()
                .build();        
        app.synth();
    }
}
