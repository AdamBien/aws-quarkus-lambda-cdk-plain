package airhacks.functionurl.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;

public class FunctionURLStack extends Stack {


    public FunctionURLStack(InfrastructureBuilder builder) {
        super(builder.construct(), builder.stackId());
        var quarkusLambda = new QuarkusLambda(this, builder.functionZipLocation(), builder.functionName(),
                builder.functionHandler(), builder.ram(), builder.isSnapStart(),
                builder.configuration());
        var function = quarkusLambda.getFunction();
        var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.NONE)
                .build());
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();

    }
}
