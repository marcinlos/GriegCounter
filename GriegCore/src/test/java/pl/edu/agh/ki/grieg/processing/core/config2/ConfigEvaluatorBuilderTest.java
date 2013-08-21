package pl.edu.agh.ki.grieg.processing.core.config2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConfigEvaluatorBuilderTest {
    
    private ConfigEvaluatorBuilder builder;
    
    @Before
    public void setup() {
        builder = new ConfigEvaluatorBuilder();
    }

    @Test
    public void canCreateWithDefaultValues() {
        assertNotNull(builder.build());
    }

}
