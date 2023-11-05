package airhacks.functionurl.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import airhacks.InfrastructureBuilder;

import org.junit.jupiter.api.Test;

import software.amazon.awscdk.App;

public class FunctionURLStackTest {

    private final static ObjectMapper JSON = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void functionURLSynth() throws IOException {
        App app = new App();
        var stack = new InfrastructureBuilder(app, "function-url")
        .functionName("functionurl-test")
        .buildFunctionURLStack();

        // synthesize the stack to a CloudFormation template
        var actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());

        // Update once resources have been added to the stack
        assertThat(actual.get("Resources")).isNotEmpty();
    }

}
