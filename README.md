# MicroProfile with Quarkus as AWS Lambda Function deployed with Cloud Development Kit (CDK)

A lean starting point for building, testing and deploying Quarkus MicroProfile applications deployed as AWS Lambda behind an AWS API Gateway.

# TL;DR

A Quarkus MicroProfile application:

```java

@Path("hello")
@ApplicationScoped
public class GreetingResource {

    @Inject
    Greeter greeter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return this.greeter.greetings();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void hello(String message) {
        this.greeter.greetings(message);
    }
}
```
...with an additional dependency / [extension](https://quarkus.io/guides/amazon-lambda-http):

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-amazon-lambda-rest</artifactId>
</dependency>
```

...deployed with AWS Cloud Development Kit:

```java

    Function createUserListenerFunction(String functionName,String functionHandler, 
    Map<String,String> configuration, int memory, int maximumConcurrentExecution, int timeout) {

        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../lambda/target/function.zip"))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .reservedConcurrentExecutions(maximumConcurrentExecution)
                .build();
    }
```