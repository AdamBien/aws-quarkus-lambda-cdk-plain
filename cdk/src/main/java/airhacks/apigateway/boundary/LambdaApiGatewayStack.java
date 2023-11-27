package airhacks.apigateway.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.apigateway.control.APIGatewayIntegrations;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class LambdaApiGatewayStack extends Stack {

    public static class LambdaApiGatewayBuilder {
        private InfrastructureBuilder builder;
        private boolean httpApiGateway;
        private boolean privateVPCAccessibility;
        private String vpcId;

        public LambdaApiGatewayBuilder(InfrastructureBuilder builder) {
            this.builder = builder;
        }

        public LambdaApiGatewayBuilder withRestAPI() {
            this.httpApiGateway = false;
            return this;
        }

        public LambdaApiGatewayBuilder withPrivateVPCAccessibility() {
            return withPrivateVPCAccessibility(null);
        }

        public LambdaApiGatewayBuilder withPrivateVPCAccessibility(String vpcId) {
            this.vpcId = vpcId;
            this.privateVPCAccessibility = true;
            return this;
        }

        InfrastructureBuilder infrastructureBuilder() {
            return this.builder;
        }

        public String appName(){
            return this.builder.stackId();
        }

        public boolean isHttpApiGateway() {
            return this.httpApiGateway;
        }

        public boolean isPrivateVPCVisibility() {
            return this.privateVPCAccessibility;
        }

        public Construct construct() {
            return this.builder.construct();
        }

        public String vpcId() {
            return this.vpcId;
        }

        public String stackId() {
            return this.builder.stackId();
        }

        public LambdaApiGatewayStack build() {
            return new LambdaApiGatewayStack(this);
        }

    }

    public LambdaApiGatewayStack(LambdaApiGatewayBuilder builder) {
        super(builder.construct(), builder.stackId());
        var infrastructureBuilder = builder.infrastructureBuilder();
        var quarkusLambda = new QuarkusLambda(this, infrastructureBuilder.functionZipLocation(),
                infrastructureBuilder.functionName(),
                infrastructureBuilder.functionHandler(), infrastructureBuilder.ram(),
                infrastructureBuilder.isSnapStart(),
                infrastructureBuilder.timeout(),
                infrastructureBuilder.configuration());
        new APIGatewayIntegrations(this, builder, quarkusLambda.getFunction());
    }
}
