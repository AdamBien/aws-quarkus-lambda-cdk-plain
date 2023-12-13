package airhacks.lambda.control;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import airhacks.ConventionalDefaults;
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

public final class QuarkusLambda extends Construct {

    static Map<String,String> RUNTIME_CONFIGURATION = Map.of(
            "JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1");

    IFunction function;

    public QuarkusLambda(Construct scope, String functionZip,String functionName,String lambdaHandler, int ramInMb,boolean snapStart,int timeout,Map<String,String> applicationConfiguration) {
        super(scope, "QuarkusLambda");
        var configuration = mergeWithRuntimeConfiguration(applicationConfiguration);
        this.function = createFunction(this,functionZip,functionName, lambdaHandler, configuration, ramInMb, timeout,snapStart);
        if (snapStart){ 
            var version = setupSnapStart(this.function);
            this.function = createAlias(version);
        }
    }

    public QuarkusLambda(Construct scope, String functionName,Map<String, String> configuration) {
        this(scope,ConventionalDefaults.functionZip,functionName,ConventionalDefaults.quarkusFunctionHandler,1700,false,ConventionalDefaults.lambdaTimeout,configuration);
    }   

    Version setupSnapStart(IFunction function) {
        var defaultChild = function.getNode().getDefaultChild();
        if (defaultChild instanceof CfnFunction cfnFunction) {
            cfnFunction.addPropertyOverride("SnapStart", Map.of("ApplyOn", "PublishedVersions"));
        }
        //a fresh logicalId enforces code redeployment
        var uniqueLogicalId = "SnapStartVersion_"+LocalDateTime.now().toString();
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

    public static IFunction createFunction(Construct scope,String functionZip,String functionName, String functionHandler, Map<String, String> configuration, int memory,
            int timeout,boolean snapStart) {
        var architecture = snapStart?Architecture.X86_64:Architecture.ARM_64;
        return Function.Builder.create(scope, functionName)
                .runtime(Runtime.JAVA_21)
                .architecture(architecture)
                .code(Code.fromAsset(functionZip))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .build();
    }

    static Map<String,String> mergeWithRuntimeConfiguration(Map<String,String> applicationConfiguuration){
        var configuration = new HashMap<>(RUNTIME_CONFIGURATION);
        configuration.putAll(applicationConfiguuration);
        return configuration;
    }

    public IFunction getFunction() {
        return this.function;
    }
}