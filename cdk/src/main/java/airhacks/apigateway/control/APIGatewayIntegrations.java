package airhacks.apigateway.control;

import java.util.List;

import airhacks.apigateway.boundary.LambdaApiGatewayStack.LambdaApiGatewayBuilder;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.services.apigateway.EndpointConfiguration;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.VpcLink;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.InterfaceVpcEndpoint;
import software.amazon.awscdk.services.ec2.InterfaceVpcEndpointAwsService;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.lambda.IFunction;
import software.constructs.Construct;

public class APIGatewayIntegrations extends Construct {

    public APIGatewayIntegrations(Construct scope, LambdaApiGatewayBuilder builder, IFunction function) {
        super(scope, "APIGatewayIntegration");

        if (builder.isHttpApiGateway())
            integrateWithHTTPApiGateway(function);
        else
            integrateWithRestApiGateway(function, builder);

        CfnOutput.Builder.create(this, "FunctionArnOutput").value(function.getFunctionArn()).build();
    }

    /**
     * https://docs.aws.amazon.com/apigateway/latest/developerguide/apigateway-rest-api.html
     */
    void integrateWithRestApiGateway(IFunction function, LambdaApiGatewayBuilder builder) {
        if (builder.isPrivateVPCVisibility()) {
            var vpc = this.getVPC(builder.appName());
            this.integrateWithPrivateRestApiGateway(function, vpc);
            return;

        }
        var apiGateway = LambdaRestApi.Builder
                .create(this, "RestApiGateway")
                .handler(function)
                .build();

        CfnOutput.Builder.create(this, "RestApiGatewayUrlOutput").value(apiGateway.getUrl()).build();

    }

    Vpc getVPC(String vpcName) {
        var privateVPC = new PrivateVPC(this,vpcName);
        return privateVPC.getVpc();
    }

    /**
     * https://docs.aws.amazon.com/apigateway/latest/developerguide/apigateway-private-apis.html
     */
    void integrateWithPrivateRestApiGateway(IFunction function, IVpc vpc) {
        var apiGateway = LambdaRestApi.Builder
                .create(this, "RestApiGateway")
                .endpointConfiguration(EndpointConfiguration
                        .builder()
                        .types(List.of(EndpointType.PRIVATE))
                        .build())
                .handler(function)
                .policy(IAMPolicy.restAPI(vpc))
                .build();

        var apiGatewayEndpoint = InterfaceVpcEndpoint.Builder
                .create(this, "ApiGatewayEndpoint")
                .vpc(vpc)
                .service(InterfaceVpcEndpointAwsService.APIGATEWAY)
                .build();

        CfnOutput.Builder.create(this, "RestApiGatewayUrlOutput").value(apiGateway.getUrl()).build();

    }

    /**
     * https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api.html
     */
    void integrateWithHTTPApiGateway(IFunction function) {
        var lambdaIntegration = HttpLambdaIntegration.Builder.create("HttpApiGatewayIntegration", function).build();
        var httpApiGateway = HttpApi.Builder.create(this, "HttpApiGatewayIntegration")
                .defaultIntegration(lambdaIntegration)
                .build();
        var url = httpApiGateway.getUrl();
        CfnOutput.Builder.create(this, "HttpApiGatewayUrlOutput").value(url).build();
        CfnOutput.Builder.create(this, "HttpApiGatewayCurlOutput").value("curl -i " + url + "hello").build();
    }

}
