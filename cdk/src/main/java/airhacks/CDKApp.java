package airhacks;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;



public class CDKApp {
    public static void main(final String[] args) {

            var app = new App();
            var appName = "quarkus-api-gateway-lambda-cdk";
            Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda");
            Tags.of(app).add("environment","development");
            Tags.of(app).add("application", appName);

            var stackProps = StackProps.builder()
                    .build();
        
        new CDKStack(app, appName, stackProps);
        app.synth();
    }
}
