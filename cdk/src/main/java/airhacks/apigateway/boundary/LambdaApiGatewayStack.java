package airhacks.apigateway.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.apigateway.control.APIGatewayIntegrations;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class LambdaApiGatewayStack extends Stack {

    static String FUNCTION_NAME  = "airhacks_lambda_gretings_boundary_Greetings";

    public static class LambdaApiGatewayBuilder{
        private InfrastructureBuilder builder;
        private boolean httpApiGateway;
        
        public LambdaApiGatewayBuilder(InfrastructureBuilder builder){
            this.builder = builder;
        }

        LambdaApiGatewayBuilder withRestAPI(){
            this.httpApiGateway = false;
            return this;
        }


        InfrastructureBuilder infrastructureBuilder(){
            return this.builder;
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
