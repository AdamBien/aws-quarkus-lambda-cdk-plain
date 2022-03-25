package airhacks;

import java.util.Map;

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


    static Map<String, String> configuration = Map.of("message", "hello, quarkus as AWS Lambda");
    static String functionName  = "airhacks_lambda_gretings_boundary_Greetings";
    static String lambdaHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
    static int memory = 1024; //~0.5 vCPU
    static int maxConcurrency = 2;
    static int timeout = 10;

    public LambdaStack(Construct scope, String id, StackProps props, boolean httpAPIGatewayIntegration) {
        super(scope, id+"-lambda", props);
        
        var function = createFunction(functionName, lambdaHandler, configuration, memory, maxConcurrency, timeout);

        if(httpAPIGatewayIntegration)
            integrateWithHTTPApiGateway(function);
        else
            integrateWithRestApiGateway(function);

        CfnOutput.Builder.create(this, "FunctionHttpApiIntegration").value(String.valueOf(httpAPIGatewayIntegration)).build();
        CfnOutput.Builder.create(this, "FunctionArnOutput").value(function.getFunctionArn()).build();

    }

    void integrateWithRestApiGateway(Function function){
        var apiGateway = LambdaRestApi.Builder.create(this, "RestApiGateway").handler(function).build();
        CfnOutput.Builder.create(this, "RestApiGatewayUrlOutput").value(apiGateway.getUrl()).build();

    }

    void integrateWithHTTPApiGateway(Function function){
        var lambdaIntegration = HttpLambdaIntegration.Builder.create("HttpApiGatewayIntegration",function).build();
        var httpApiGateway =  HttpApi.Builder.create(this, "HttpApiGatewayIntegration").defaultIntegration(lambdaIntegration).build();
        CfnOutput.Builder.create(this, "HttpApiGatewayUrlOutput").value(httpApiGateway.getUrl()).build();
    }

    Function createFunction(String functionName,String functionHandler, Map<String,String> configuration, int memory, int maximumConcurrentExecution, int timeout) {
        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_11)
                .architecture(Architecture.ARM_64)
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
