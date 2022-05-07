package airhacks.apigateway.control;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.lambda.IFunction;
import software.constructs.Construct;

public class APIGatewayIntegrations extends Construct {

    public APIGatewayIntegrations(Construct scope, boolean httpAPIGatewayIntegration, IFunction function) {
        super(scope, "APIGatewayIntegration");

        if (httpAPIGatewayIntegration)
            integrateWithHTTPApiGateway(function);
        else
            integrateWithRestApiGateway(function);

        CfnOutput.Builder.create(this, "FunctionHttpApiIntegration").value(String.valueOf(httpAPIGatewayIntegration))
                .build();
        CfnOutput.Builder.create(this, "FunctionArnOutput").value(function.getFunctionArn()).build();
    }

    /**
     * https://docs.aws.amazon.com/apigateway/latest/developerguide/apigateway-rest-api.html
     */
    void integrateWithRestApiGateway(IFunction function) {
        var apiGateway = LambdaRestApi.Builder.create(this, "RestApiGateway").handler(function).build();
        CfnOutput.Builder.create(this, "RestApiGatewayUrlOutput").value(apiGateway.getUrl()).build();

    }

    /**
     * https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api.html
     */
    void integrateWithHTTPApiGateway(IFunction function) {
        var lambdaIntegration = HttpLambdaIntegration.Builder.create("HttpApiGatewayIntegration", function).build();
        var httpApiGateway = HttpApi.Builder.create(this, "HttpApiGatewayIntegration")
                .defaultIntegration(lambdaIntegration).build();
        var url = httpApiGateway.getUrl();
        CfnOutput.Builder.create(this, "HttpApiGatewayUrlOutput").value(url).build();
        CfnOutput.Builder.create(this, "HttpApiGatewayCurlOutput").value("curl -i " + url + "hello").build();
    }

}
