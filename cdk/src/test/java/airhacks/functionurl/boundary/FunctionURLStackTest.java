package airhacks.functionurl.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import airhacks.FunctionZip;
import airhacks.InfrastructureBuilder;

import org.junit.jupiter.api.Test;

import software.amazon.awscdk.App;

public class FunctionURLStackTest {

    private final static ObjectMapper JSON = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    void stack() throws IOException {
        var mockFunctionZip = FunctionZip.createEmptyFunctionZip();
        App app = new App();
        var stack = new InfrastructureBuilder(app, "function-url")
                .functionName("functionurl-test")
                .functionZip(mockFunctionZip)
                .functionURLBuilder()
                .build();

        // synthesize the stack to a CloudFormation template
        var actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());

        // Update once resources have been added to the stack
        assertThat(actual.get("Resources")).isNotEmpty();
    }

}
