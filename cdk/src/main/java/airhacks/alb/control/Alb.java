package airhacks.alb.control;

import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancer;
import software.constructs.Construct;

public class Alb extends Construct {

    ApplicationLoadBalancer applicationLoadBalancer;

    public Alb(Construct scope, Vpc vpc, String loadBalancerName) {
        super(scope, "ApplicationLoadBalancer");

        this.applicationLoadBalancer = ApplicationLoadBalancer.Builder.create(this, "ELB")
                .internetFacing(true)
                .loadBalancerName(loadBalancerName)
                .vpc(vpc)
                .build();       
    }

    public ApplicationLoadBalancer getApplicationLoadBalancer(){
        return this.applicationLoadBalancer;
    }
}
