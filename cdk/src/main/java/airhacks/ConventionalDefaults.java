package airhacks;

public interface ConventionalDefaults {

    String functionZip = "../lambda/target/function.zip";
    String quarkusFunctionHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
    int lambdaTimeout = 10;

    
}
