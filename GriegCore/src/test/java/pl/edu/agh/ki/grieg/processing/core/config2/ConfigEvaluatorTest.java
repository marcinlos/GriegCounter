package pl.edu.agh.ki.grieg.processing.core.config2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;

@RunWith(MockitoJUnitRunner.class)
public class ConfigEvaluatorTest {
    
    @Mock private AssemblerDefinition assemblerDefinition;
    @Mock private PipelineAssembler assembler;
    @Mock private Evaluator evaluator;
    @Mock private ErrorHandler handler;
    
    @InjectMocks private ConfigEvaluator configEvaluator;
    
    @Before
    public void setup() throws ConfigException {
        when(assemblerDefinition.createAssembler()).thenReturn(assembler);
    }

    @Test
    public void canProcessEmptyConfig() throws Exception {
        Map<String, PropertyNode> propertyNodes = Maps.newHashMap();
        PipelineNode pipeline = new PipelineNode();
        
        ConfigNode node = new ConfigNode(propertyNodes, pipeline);
        Config config = configEvaluator.evaluate(node);
//        assertEquals(assembler, config.createAssembler());
        assertTrue(config.buildProperties().isEmpty());
    }

}
