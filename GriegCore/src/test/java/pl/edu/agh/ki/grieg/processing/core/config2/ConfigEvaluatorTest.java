package pl.edu.agh.ki.grieg.processing.core.config2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ValueNode;

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
        List<PropertyNode> propertyNodes = ImmutableList.of();
        PipelineNode pipeline = new PipelineNode();
        
        ConfigNode node = new ConfigNode(propertyNodes, pipeline);
        Config config = configEvaluator.evaluate(node);
//        assertEquals(assembler, config.createAssembler());
        assertTrue(config.buildProperties().isEmpty());
        verifyZeroInteractions(handler);
    }
    
    
    @Test
    public void canProcessConfigWithProperties() throws Exception {
        ValueNode first = new PrimitiveValueNode("3", int.class);
        PrimitiveValueNode second = new PrimitiveValueNode("some", String.class);
        CompleteValueNode third = new CompleteValueNode(12.34);
        List<PropertyNode> propertyNodes = ImmutableList.of(
                new PropertyNode("size", first),
                new PropertyNode("name", second),
                new PropertyNode("sth", third));
        PipelineNode pipeline = new PipelineNode();

        when(evaluator.evaluate(first)).thenReturn(3);
        when(evaluator.evaluate(second)).thenReturn("some");
        when(evaluator.evaluate(third)).thenReturn(12.34);

        ConfigNode node = new ConfigNode(propertyNodes, pipeline);
        Config config = configEvaluator.evaluate(node);

        Map<String, Object> expected = ImmutableMap.<String, Object> builder()
                .put("size", 3)
                .put("name", "some")
                .put("sth", 12.34)
                .build();
        Map<String, Object> actual = config.buildProperties().asMap();
        assertEquals(expected, actual);
        verifyZeroInteractions(handler);
    }

}
