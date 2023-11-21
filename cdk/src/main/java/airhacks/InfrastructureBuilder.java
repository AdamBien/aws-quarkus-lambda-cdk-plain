package airhacks;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import airhacks.apigateway.boundary.LambdaApiGatewayStack;
import airhacks.functionurl.boundary.FunctionURLStack;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.constructs.Construct;

public class InfrastructureBuilder {

    private FunctionUrlAuthType authType = FunctionUrlAuthType.NONE;
    private Construct construct;
    private String stackId;
    private boolean snapStart = false;
    private String functionName;
    private String functionHandler = ConventionalDefaults.quarkusFunctionHandler;
    private Map<String, String> configuration = Map.of();
    private String functionZipLocation = ConventionalDefaults.functionZip;
    final int ONE_CPU = 1700;
    private int ram = ONE_CPU;
    private int timeout = ConventionalDefaults.lambdaTimeout;

    public InfrastructureBuilder(Construct construct, String stackNamePrefix) {
        this.construct = construct;
        this.stackId = stackNamePrefix.toLowerCase();
    }

    public InfrastructureBuilder functionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public InfrastructureBuilder functionHandler(String handler) {
        this.functionHandler = handler;
        return this;
    }

    public InfrastructureBuilder ram(int ram) {
        this.ram = ram;
        return this;
    }

    public InfrastructureBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public InfrastructureBuilder withOneCPU() {
        this.ram = ONE_CPU;
        return this;
    }

    public InfrastructureBuilder withHalfCPU() {
        this.ram = ONE_CPU / 2;
        return this;
    }

    public InfrastructureBuilder withTwoCPUs() {
        this.ram = ONE_CPU * 2;
        return this;
    }

    public InfrastructureBuilder withIAMAuth() {
        this.authType = FunctionUrlAuthType.AWS_IAM;
        return this;
    }

    /**
     * 
     * @param location the full path to the function.zip archive.
     * @return
     */
    public InfrastructureBuilder functionZip(String location) {
        verifyFunctionZip(location);
        this.functionZipLocation = location;
        return this;
    }

    public InfrastructureBuilder quarkusLambdaProjectLocation(String location) {
        this.functionZipLocation = location + "/target/function.zip";
        return this;
    }

    public InfrastructureBuilder snapStart(boolean snapStart) {
        this.snapStart = snapStart;
        return this;
    }

    public InfrastructureBuilder configuration(Map<String, String> configuration) {
        this.configuration = configuration;
        return this;
    }

    public FunctionURLStack buildFunctionURLStack() {
        Objects.requireNonNull(this.functionName, "Function name is required");
        appendToId("function-url-stack");
        return new FunctionURLStack(this);
    }

    public LambdaApiGatewayStack buildLambdaApiGatewayStack() {
        Objects.requireNonNull(this.functionName, "Function name is required");
        appendToId("lambda-apigateway-stack");
        return new LambdaApiGatewayStack(this);
    }

    static boolean verifyFunctionZip(String functionZipFile) {
        if (!functionZipFile.endsWith("function.zip")) {
            throw new IllegalArgumentException("File must end with function.zip, but was: " + functionZipFile);
        }
        var exists = Files.exists(Path.of(functionZipFile));
        if (!exists) {
            throw new IllegalArgumentException("function.zip not found at: " + functionZipFile);
        }
        return true;
    }

    public Construct construct() {
        return construct;
    }

    public String stackId() {
        return stackId;
    }

    void appendToId(String ending) {
        this.stackId = stackId + "-" + ending;
    }

    public boolean isSnapStart() {
        return snapStart;
    }

    public String functionName() {
        return functionName;
    }

    public String functionHandler() {
        return functionHandler;
    }

    public Map<String, String> configuration() {
        return configuration;
    }

    public String functionZipLocation() {
        return functionZipLocation;
    }

    public int ram() {
        return ram;
    }

    public int timeout() {
        return this.timeout;
    }

    public FunctionUrlAuthType functionUrlAuthType() {
        return this.authType;
    }
}
