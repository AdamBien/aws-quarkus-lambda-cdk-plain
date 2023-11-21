package airhacks.functionurl.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class FunctionURLStack extends Stack {

    public static class FunctionURLBuilder {
        private InfrastructureBuilder infrastructureBuilder;
        private FunctionUrlAuthType authType = FunctionUrlAuthType.NONE;


        public FunctionURLBuilder(InfrastructureBuilder infrastructureBuilder) {
            this.infrastructureBuilder = infrastructureBuilder;
        }

        public Construct construct() {
            return this.infrastructureBuilder.construct();
        }

        public String stackId() {
            return this.infrastructureBuilder.stackId();
        }

        public FunctionURLBuilder withIAMAuth() {
            this.authType = FunctionUrlAuthType.AWS_IAM;
            return this;
        }

        public FunctionURLStack build() {
            return new FunctionURLStack(this);
        }

    }

    public FunctionURLStack(FunctionURLBuilder builder) {
        super(builder.construct(), builder.stackId());
        var infrastructureBuilder = builder.infrastructureBuilder;
        var quarkusLambda = new QuarkusLambda(this, infrastructureBuilder.functionZipLocation(),
                infrastructureBuilder.functionName(),
                infrastructureBuilder.functionHandler(), infrastructureBuilder.ram(),
                infrastructureBuilder.isSnapStart(),
                infrastructureBuilder.timeout(),
                infrastructureBuilder.configuration());
        var function = quarkusLambda.getFunction();
        var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(builder.authType)
                .build());
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();

    }
}
