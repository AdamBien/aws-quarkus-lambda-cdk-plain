package airhacks.apigateway.boundary;

import airhacks.apigateway.control.APIGatewayIntegrations;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class LambdaApiGatewayStack extends Stack {

    //true -> integrates with Http API, false -> integrates with REST API
    static boolean HTTP_API_GATEWAY_INTEGRATION = true;
    static String FUNCTION_NAME  = "airhacks_lambda_gretings_boundary_Greetings";

    public LambdaApiGatewayStack(Construct scope, String id) {
        super(scope, id+"-apigateway-stack");
        var quarkuLambda = new QuarkusLambda(this,FUNCTION_NAME);
        new APIGatewayIntegrations(this, HTTP_API_GATEWAY_INTEGRATION, quarkuLambda.getFunction());
    }





}
