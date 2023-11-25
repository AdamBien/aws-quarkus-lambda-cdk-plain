package airhacks.apigateway.control;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.iam.AnyPrincipal;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;

public interface IAMPolicy {

    static PolicyDocument restAPI(IVpc vpc){
        return PolicyDocument.Builder
        .create()
        .statements(List.of(allowStatement(),denyNotFrom(vpc.getVpcId())))
        .build();

    }

    static PolicyStatement allowStatement() {
        return PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .principals(List.of(new AnyPrincipal()))
                .actions(List.of("execute-api:Invoke"))
                .resources(List.of("execute-api:/*"))
                .build();
    }

    static PolicyStatement denyNotFrom(String vpcId) {
        return PolicyStatement.Builder
                .create()
                .effect(Effect.DENY)
                .principals(List.of(new AnyPrincipal()))
                .actions(List.of("execute-api:Invoke"))
                .resources(List.of("execute-api:/*"))
                .conditions(Map.of("StringNotEquals",
                        Map.of("aws:SourceVpc", vpcId)))
                .build();
    }

}
