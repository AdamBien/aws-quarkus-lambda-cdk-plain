package airhacks.lambda.boundary;

import java.util.Map;

import airhacks.lambda.control.APIGatewayIntegrations;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class LambdaStack extends Stack {

    static String FUNCTION_NAME  = "airhacks_lambda_gretings_boundary_Greetings";



    public LambdaStack(Construct scope, String id, boolean httpAPIGatewayIntegration) {
        super(scope, id);
        var quarkuLambda = new QuarkusLambda(this,FUNCTION_NAME);
        var apiGatewayIntegration = new APIGatewayIntegrations(this, httpAPIGatewayIntegration, quarkuLambda.getFunction());

    }





}
