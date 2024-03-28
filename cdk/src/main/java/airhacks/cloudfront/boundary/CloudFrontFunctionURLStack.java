package airhacks.cloudfront.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.functionurl.boundary.FunctionURLStack;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.cloudfront.AllowedMethods;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.CachePolicy;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.OriginProtocolPolicy;
import software.amazon.awscdk.services.cloudfront.SSLMethod;
import software.amazon.awscdk.services.cloudfront.SecurityPolicyProtocol;
import software.amazon.awscdk.services.cloudfront.ViewerProtocolPolicy;
import software.amazon.awscdk.services.cloudfront.origins.HttpOrigin;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class CloudFrontFunctionURLStack extends Stack {
    public static class CloudFrontFunctionURLBuilder {
        private InfrastructureBuilder infrastructureBuilder;
        private FunctionUrlAuthType authType = FunctionUrlAuthType.NONE;

        public CloudFrontFunctionURLBuilder(InfrastructureBuilder infrastructureBuilder) {
            this.infrastructureBuilder = infrastructureBuilder;
        }

        public Construct construct() {
            return this.infrastructureBuilder.construct();
        }

        public String stackId() {
            return this.infrastructureBuilder.stackId();
        }

        public CloudFrontFunctionURLStack build() {
            return new CloudFrontFunctionURLStack(this);
        }

    }

    public CloudFrontFunctionURLStack(CloudFrontFunctionURLBuilder builder) {
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
        var distribution = Distribution.Builder.create(this, "FunctionURLDistribution")
                .minimumProtocolVersion(SecurityPolicyProtocol.SSL_V3)
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new HttpOrigin(functionUrl.getUrl()))
                        .allowedMethods(AllowedMethods.ALLOW_ALL)
                        .viewerProtocolPolicy(ViewerProtocolPolicy.REDIRECT_TO_HTTPS)
                        .cachePolicy(CachePolicy.CACHING_DISABLED)
                        .build())
                .build();
        CfnOutput.Builder.create(this, "CloudFrontDistributionDomainNameOutput").value(distribution.getDistributionDomainName()).build();

    }

}
