package airhacks.lambda.control;

import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class QuarkusLambda extends Construct{

    static Map<String, String> configuration = Map.of("message", "hello, quarkus as AWS Lambda");
    static String lambdaHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
    static int memory = 1024; //~0.5 vCPU
    static int maxConcurrency = 2;
    static int timeout = 10;
    IFunction function;

    public QuarkusLambda(Construct scope,String functionName) {
        super(scope, "QuarkusLambda");
        this.function = createFunction(functionName, lambdaHandler, configuration, memory, maxConcurrency, timeout);
    }

    IFunction createFunction(String functionName,String functionHandler, Map<String,String> configuration, int memory, int maximumConcurrentExecution, int timeout) {
        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_11)
                .architecture(Architecture.ARM_64)
                .code(Code.fromAsset("../lambda/target/function.zip"))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .reservedConcurrentExecutions(maximumConcurrentExecution)
                .build();
    }    

    public IFunction getFunction(){
        return this.function;
    }
}