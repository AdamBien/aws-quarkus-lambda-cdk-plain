# MicroProfile with Quarkus as AWS Lambda Function deployed with Cloud Development Kit (CDK) v2 for Java

A lean starting point for building, testing and deploying Quarkus MicroProfile applications deployed as AWS Lambda behind API Gateway.
The business logic, as well as, the Infrastructure as Code deployment are implemented with Java.

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
...with an additional dependency / [extension](https://quarkus.io/guides/amazon-lambda-http) for AWS REST APIs Gateway:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-amazon-lambda-rest</artifactId>
</dependency>
```

or HTTP APIs Gateway (default configuration):

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-amazon-lambda-http</artifactId>
</dependency>
```

...deployed with AWS Cloud Development Kit:

```java

Function createFunction(String functionName,String functionHandler, 
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
You choose between HTTP APIs gateway and REST APIs gateway with the `httpAPIGatewayIntegration` variable:

``` java
public class CDKApp {
    public static void main(final String[] args) {

            var app = new App();
            var appName = "quarkus-apigateway-lambda-cdk";
            Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda");
            Tags.of(app).add("environment","development");
            Tags.of(app).add("application", appName);

            var httpAPIGatewayIntegration = true;
            new CDKStack(app, appName, true);
            app.synth();
        }
    }
}
```

## Prerequisites

## Java

1. Java / openJDK is installed
2. [Maven](https://maven.apache.org/) is installed

## AWS 

Same installation as [aws-cdk-plain](https://github.com/AdamBien/aws-cdk-plain):

0. For max convenience use the [`default` profile](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html). A profile named `default` doesn't have to be specificed with the `--profile` flag or configured in CDK applications.
1. Install [AWS CDK CLI](https://docs.aws.amazon.com/cdk/latest/guide/getting_started.html)
2. [`cdk boostrap --profile YOUR_AWS_PROFILE`](https://docs.aws.amazon.com/cdk/latest/guide/bootstrapping.html)

This template ships with AWS HTTP APIs Gateway. REST APIs Gateway is also supported. You can switch between both by using the corresponding extension (see [Choosing between HTTP APIs and REST APIs](https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-vs-rest.html). 

Private APIs are only supported by [REST API Gateway](https://docs.aws.amazon.com/apigateway/latest/developerguide/apigateway-private-apis.html).

You can also build AWS Lambda `function.zip` and executable Quarkus JAR by extracting the extension into a Maven profile. Checkout: [https://adambien.blog/roller/abien/entry/hybrid_microprofile_deployments_with_quarkus](https://adambien.blog/roller/abien/entry/hybrid_microprofile_deployments_with_quarkus).

See you at: [airhacks.live](https://airhacks.live)

# in action

## full build

Build the Quarkus project `lambda` and deploy it with `cdk` as AWS Lambda:

```
cd lambda
./buildAndDeployDontAsk.sh
```

## continuous and accelerated deployment

To continuously deploy the AWS Lambda at any changes, perform: 

```
cd cdk
cdk watch
```

Now on every: `mvn package` in `lambda` directory / project the JAX-RS application is re-deployed automatically.

## local deployment

You can run the `lambda` project as regular Quarkus application with:

`mvn compile quarkus:dev`

The application is available under: `http://localhost:8080/hello`

## Deploying MicroProfile / Quarkus Application as AWS Lambda with Java AWS CDK

[![Deploying MicroProfile / Quarkus Application as AWS Lambda with Java AWS CDK](https://i.ytimg.com/vi/NA0WjIgp4CQ/mqdefault.jpg)](https://www.youtube.com/embed/NA0WjIgp4CQ?rel=0)


## Accelarating deployments with CDK v2 Watch


Using `cdk watch` for faster deployments

[![Accelerating Deployment with CDK v2 Watch](https://i.ytimg.com/vi/SK7ic9wTYqU/mqdefault.jpg)](https://www.youtube.com/embed/SK7ic9wTYqU?rel=0)

See you at: [airhacks.live](https://airhacks.live)
