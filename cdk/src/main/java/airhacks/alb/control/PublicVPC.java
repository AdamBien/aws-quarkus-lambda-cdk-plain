package airhacks.alb.control;

import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class PublicVPC extends Construct {

    Vpc vpc;

    public PublicVPC(Construct scope) {
        super(scope, "PublicVPC");
        this.vpc = Vpc.Builder.create(this, "VPC")
                .cidr("10.0.0.0/16")
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .natGateways(0)
                .maxAzs(2)
                .build();
    }

    public Vpc getVpc(){
        return this.vpc;
    }

}
