package airhacks.codepipeline;

import java.util.Arrays;
import java.util.List;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.StageProps;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodePipelineSource;
import software.amazon.awscdk.pipelines.ConnectionSourceOptions;
import software.amazon.awscdk.pipelines.ShellStep;
import software.constructs.Construct;

public class CodePipelineStack extends Stack{

    public CodePipelineStack(Construct scope,String appName,StackProps stackProps,String repository,String branch,String codestarConnectionARN){
        var stageProps = StageProps.builder().env(stackProps.getEnv()).build();
        var pipeline = CodePipeline.Builder.create(this, "CodePipeline")
                                 .pipelineName(appName)
                                 .synth(ShellStep.Builder.create("Synth")
                                        .input(source(repository, branch, codestarConnectionARN))
                                        .commands(List.of("npm install -g aws-cdk", "cdk synth"))
                                        .build())
                                 .build();                                 
            pipeline.addStage(new QuarkusLambdaStage(this, appName, stackProps,stageProps));

    }

    CodePipelineSource source(String repository,String branch,String codestarConnectionARN){
        return CodePipelineSource.connection(repository, branch,ConnectionSourceOptions.builder()
        .connectionArn(codestarConnectionARN)
        .build());


    }
   
}
