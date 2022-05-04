package airhacks;

import airhacks.alb.boundary.LambdaAlbStack;
import airhacks.apigateway.boundary.LambdaApiGatewayStack;
import airhacks.functionurl.boundary.FunctionURLStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;



public class CDKApp {

    static StackProps createStackProperties() {
        var account = System.getenv("CDK_DEPLOY_ACCOUNT");
        var region  = System.getenv("CDK_DEPLOY_REGION");

        if(account == null)
            return StackProps.builder().build();

        var environment =  Environment.builder()
                .account(account)
                .region(region)
                .build();
        return StackProps.builder().env(environment).build();
    }

    public static void main(final String[] args) {

            var app = new App();
            var appName = "quarkus-lambda";

            Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda");
            Tags.of(app).add("environment","development");
            Tags.of(app).add("application", appName);

            var stackProps = createStackProperties();

            new FunctionURLStack(app,appName);
            //new LambdaApiGatewayStack(app, appName);
            //new LambdaAlbStack(app,appName);
            app.synth();
        }
}
