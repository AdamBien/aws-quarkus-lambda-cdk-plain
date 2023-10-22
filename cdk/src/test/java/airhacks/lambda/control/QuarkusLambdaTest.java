package airhacks.lambda.control;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
 
public class QuarkusLambdaTest {


    @Test
    public void testMergeWithRuntimeConfiguration() {
        var actual = QuarkusLambda.mergeWithRuntimeConfiguration(Map.of("msg","duke"));
        assertThat(actual).containsKey("JAVA_TOOL_OPTIONS");
        assertThat(actual).containsEntry("msg", "duke");
        
    }


}