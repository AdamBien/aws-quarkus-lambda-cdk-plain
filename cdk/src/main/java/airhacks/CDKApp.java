package airhacks;

import airhacks.alb.boundary.LambdaAlbStack;
import airhacks.apigateway.boundary.LambdaApiGatewayStack;
import airhacks.functionurl.boundary.FunctionURLStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

public interface CDKApp {

    static void main(String... args) {

        var app = new App();
        var appName = "quarkus-lambda";

        Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda");
        Tags.of(app).add("environment", "development");
        Tags.of(app).add("application", appName);

        var functionURLStack = new InfrastructureBuilder(app, appName)
                .functionName("airhacks")
                .buildFunctionURLStack();
        // new LambdaApiGatewayStack(app, appName);
        // new LambdaAlbStack(app,appName);
        app.synth();
    }
}
