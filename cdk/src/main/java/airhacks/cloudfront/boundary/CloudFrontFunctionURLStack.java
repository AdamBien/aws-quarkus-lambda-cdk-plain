package airhacks.cloudfront.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.cloudfront.AllowedMethods;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.CachePolicy;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.OriginRequestPolicy;
import software.amazon.awscdk.services.cloudfront.SecurityPolicyProtocol;
import software.amazon.awscdk.services.cloudfront.ViewerProtocolPolicy;
import software.amazon.awscdk.services.cloudfront.origins.FunctionUrlOrigin;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class CloudFrontFunctionURLStack extends Stack {
    public static class CloudFrontFunctionURLBuilder {
        private InfrastructureBuilder infrastructureBuilder;

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
                .authType(FunctionUrlAuthType.NONE)
                .build());
        var functionURLOrigin = new FunctionUrlOrigin(functionUrl);
        var distribution = Distribution.Builder.create(this, "FunctionURLDistribution")
                .minimumProtocolVersion(SecurityPolicyProtocol.TLS_V1_2_2021)
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(functionURLOrigin)
                        .viewerProtocolPolicy(ViewerProtocolPolicy.HTTPS_ONLY)
                        .allowedMethods(AllowedMethods.ALLOW_ALL)
                        .cachePolicy(CachePolicy.CACHING_DISABLED)
                        .originRequestPolicy(OriginRequestPolicy.ALL_VIEWER)
                        .build())
                /**
                 * minimum policy only works with custom certificate
                 * 
                 */
                .minimumProtocolVersion(SecurityPolicyProtocol.TLS_V1_2_2021)
                .build();
        CfnOutput.Builder.create(this, "CloudFrontDistributionDomainNameOutput")
                .value(distribution.getDistributionDomainName()).build();
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();
    }

}
