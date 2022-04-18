package airhacks.alb.boundary;

import java.util.List;

import airhacks.alb.control.Alb;
import airhacks.alb.control.PublicVPC;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.elasticloadbalancingv2.AddApplicationTargetsProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.BaseApplicationListenerProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.targets.LambdaTarget;
import software.constructs.Construct;

public class LambdaAlbStack extends Stack {

    static String FUNCTION_NAME  = "airhacks_lambda_gretings_boundary_Greetings";

    public LambdaAlbStack(Construct scope, String id) {
        super(scope, id+"-alb-stack");

        var quarkuLambda = new QuarkusLambda(this,FUNCTION_NAME);
        var publicVPCConstruct = new PublicVPC(this);
        var publicVPC = publicVPCConstruct.getVpc();
        var alb = new Alb(this, publicVPC, "AirhacksLambdaLB");

        var function = quarkuLambda.getFunction();
        var lambdaTarget = new LambdaTarget(function);
        
        var loadBalancer = alb.getApplicationLoadBalancer();
        var listener = loadBalancer.addListener("Http", BaseApplicationListenerProps.builder()
                                                                            .port(80)
                                                                            .build()); 
                                                                            
        listener.addTargets("Lambda", AddApplicationTargetsProps.builder()
                                        .targets(List.of(lambdaTarget))
                                        .healthCheck(HealthCheck.builder()
                                                        .enabled(true)
                                                        .build())
                                        .build());    
        CfnOutput.Builder.create(this, "FunctionARN").value(function.getFunctionArn()).build();
        var url = loadBalancer.getLoadBalancerDnsName();
        CfnOutput.Builder.create(this, "LoadBalancerDNSName").value(url).build();
        CfnOutput.Builder.create(this, "LoadBalancerCurlOutput").value("curl -i http://"+url+"/hello").build();
    }
}
