package airhacks.codepipeline;


import airhacks.CDKStack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Stage;
import software.amazon.awscdk.StageProps;
import software.constructs.Construct;

public class QuarkusLambdaStage extends Stage{

    private CDKStack cdkStack;

    public QuarkusLambdaStage(Construct scope, String id,StackProps stackProps, StageProps stageProps) {
        super(scope, id, stageProps);
        this.cdkStack = new CDKStack(scope, id, stackProps, true);
    }


    
}
