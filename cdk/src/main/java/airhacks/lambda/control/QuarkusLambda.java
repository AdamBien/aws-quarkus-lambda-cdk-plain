package airhacks.lambda.control;

import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.CfnFunction;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.constructs.Construct;

public class QuarkusLambda extends Construct {

    static Map<String, String> configuration = Map.of(
            "message", "hello, quarkus as AWS Lambda",
            "JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1 -XX:verbose:class");
    static String lambdaHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
    static int memory = 1024; // ~0.5 vCPU
    static int timeout = 10;
    IFunction function;

    public QuarkusLambda(Construct scope, String functionName){
        this(scope,functionName,true);
    }

    public QuarkusLambda(Construct scope, String functionName, boolean snapStart) {
        super(scope, "QuarkusLambda");
        this.function = createFunction(functionName, lambdaHandler, configuration, memory, timeout,snapStart);
        if (snapStart){ 
            var version = setupSnapStart(this.function);
            this.function = createAlias(version);
        }
    }

    Version setupSnapStart(IFunction function) {
        var defaultChild = this.function.getNode().getDefaultChild();
        if (defaultChild instanceof CfnFunction cfnFunction) {
            cfnFunction.addPropertyOverride("SnapStart", Map.of("ApplyOn", "PublishedVersions"));
        }
        //a fresh logicalId enfoces code redeployment
        var uniqueLogicalId = "SnapStartVersion"+System.currentTimeMillis();
        return Version.Builder.create(this, uniqueLogicalId)
                .lambda(this.function)
                .description("SnapStart")
                .build();              
    }


    Alias createAlias(Version version){
        return Alias.Builder.create(this, "SnapstartAlias")
        .aliasName("snapstart")
        .description("this alias is required for SnapStart")
        .version(version)
        .build();
    }

    IFunction createFunction(String functionName, String functionHandler, Map<String, String> configuration, int memory,
            int timeout,boolean snapStart) {
        var architecture = snapStart?Architecture.X86_64:Architecture.ARM_64;
        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_11)
                .architecture(architecture)
                .code(Code.fromAsset("../lambda/target/function.zip"))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .build();
    }

    public IFunction getFunction() {
        return this.function;
    }
}