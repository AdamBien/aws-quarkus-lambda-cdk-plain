package airhacks.apigateway.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.apigateway.control.APIGatewayIntegrations;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class LambdaApiGatewayStack extends Stack {


    public static class LambdaApiGatewayBuilder{
        private InfrastructureBuilder builder;
        private boolean httpApiGateway;
        private boolean privateVPCAccessibility;
        
        public LambdaApiGatewayBuilder(InfrastructureBuilder builder){
            this.builder = builder;
        }

        LambdaApiGatewayBuilder withRestAPI(){
            this.httpApiGateway = false;
            return this;
        }

        LambdaApiGatewayBuilder withPrivateVPCAccessibility(){
            this.privateVPCAccessibility = true;
            return this;
        }


        InfrastructureBuilder infrastructureBuilder(){
            return this.builder;
        }


        boolean privateVPCVisibility(){
            return this.privateVPCAccessibility;
        }


        public Construct construct(){
            return this.builder.construct();
        }


        public String stackId(){
            return this.builder.stackId();
        }

        public LambdaApiGatewayStack build(){
            return new LambdaApiGatewayStack(this);
        }

    }

    public LambdaApiGatewayStack(LambdaApiGatewayBuilder builder) {
        super(builder.construct(), builder.stackId());    
        var infrastructureBuilder = builder.infrastructureBuilder();
        var quarkuLambda = new QuarkusLambda(this,infrastructureBuilder.functionName(),infrastructureBuilder.configuration());
        new APIGatewayIntegrations(this, builder.httpApiGateway, quarkuLambda.getFunction());
    }





}
