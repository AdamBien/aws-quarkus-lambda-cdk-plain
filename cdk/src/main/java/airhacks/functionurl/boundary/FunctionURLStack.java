package airhacks.functionurl.boundary;

import java.util.Map;

import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class FunctionURLStack extends Stack {

    static String FUNCTION_NAME = "airhacks_lambda_gretings_boundary_Greetings";

    public FunctionURLStack(Construct construct,String id,boolean snapStart) {
        super(construct,id+ "-function-url-stack");
        var configuration = Map.of(
            "message", "hello, quarkus as AWS Lambda",
            "JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1");

        var quarkusLambda = new QuarkusLambda(this, FUNCTION_NAME,snapStart,configuration);
        var function = quarkusLambda.getFunction();
        var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.NONE)
                .build());
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();

    }
}
