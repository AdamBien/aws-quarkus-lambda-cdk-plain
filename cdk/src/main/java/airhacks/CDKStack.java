package airhacks;

import java.util.Map;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class CDKStack extends Stack {


    static Map<String, String> configuration = Map.of("message", "hello, quarkus as AWS Lambda");
    static String functionName  = "airhacks_lambda_gretings_boundary_Greetings";
    static String lambdaHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
    static int memory = 512;
    static int maxConcurrency = 2;
    static int timeout = 10;

    public CDKStack(Construct scope, String id, StackProps props, boolean httpAPIGatewayIntegration) {
        super(scope, id, props);
        
        var function = createFunction(functionName, lambdaHandler, configuration, memory, maxConcurrency, timeout);

        if(httpAPIGatewayIntegration)
            integrateWithHTTPApiGateway(function);
        else
            integrateWithRestApiGateway(function);

        CfnOutput.Builder.create(this, "function-http-api-integration").value(String.valueOf(httpAPIGatewayIntegration)).build();
        CfnOutput.Builder.create(this, "function-output").value(function.getFunctionArn()).build();

    }

    void integrateWithRestApiGateway(Function function){
        var apiGateway = LambdaRestApi.Builder.create(this, "api-gateway").handler(function).build();
        CfnOutput.Builder.create(this, "rest-api-gateway-output").value(apiGateway.getUrl()).build();

    }

    void integrateWithHTTPApiGateway(Function function){
        var lambdaIntegration = HttpLambdaIntegration.Builder.create("http-api-gateway-integration",function).build();
        var httpApiGateway =  HttpApi.Builder.create(this, "http-api-gateway-integration").defaultIntegration(lambdaIntegration).build();
        CfnOutput.Builder.create(this, "http-api-gateway-output").value(httpApiGateway.getUrl()).build();

    }

    Function createFunction(String functionName,String functionHandler, Map<String,String> configuration, int memory, int maximumConcurrentExecution, int timeout) {
        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../lambda/target/function.zip"))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .reservedConcurrentExecutions(maximumConcurrentExecution)
                .build();
    }

}
