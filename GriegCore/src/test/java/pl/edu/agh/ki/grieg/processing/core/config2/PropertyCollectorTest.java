package pl.edu.agh.ki.grieg.processing.core.config2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;

import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ValueNode;

@RunWith(MockitoJUnitRunner.class)
public class PropertyCollectorTest {

    @Mock
    private ErrorHandler errorHandler;
    @Mock
    private Evaluator evaluator;

    @InjectMocks
    private PropertyCollector propertyCollector;

    @Test
    public void collectsAllTheProperties() throws Exception {
        PrimitiveValueNode value = new PrimitiveValueNode("", int.class);
        when(evaluator.evaluate(value)).thenReturn("sth", 13, 'p', 1.23);

        for (String name : new String[] { "prop", "value", "height", "width" }) {
            PropertyNode dummy = new PropertyNode(name, value);
            propertyCollector.consume(dummy);
        }
        ImmutableMap<String, Object> expected = ImmutableMap
                .<String, Object> builder()
                .put("prop", "sth")
                .put("value", 13)
                .put("height", 'p')
                .put("width", 1.23)
                .build();
        assertEquals(expected, propertyCollector.getProperties());
    }

    @Test
    public void handlesOneError() throws Exception {
        when(evaluator.evaluate(any(ValueNode.class)))
                .thenThrow(new ValueException(""));
        PrimitiveValueNode value = new PrimitiveValueNode("", int.class);
        propertyCollector.consume(new PropertyNode("prop", value));
        verify(errorHandler).error(any(Throwable.class));
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    public void handlesMultipleErrors() throws Exception {
        when(evaluator.evaluate(any(ValueNode.class)))
                .thenThrow(new ValueException(""));
        PrimitiveValueNode value = new PrimitiveValueNode("", int.class);
        for (int i = 0; i < 5; ++i) {
            propertyCollector.consume(new PropertyNode("prop", value));
        }
        verify(errorHandler, times(5)).error(any(Throwable.class));
        verifyNoMoreInteractions(errorHandler);
    }

    @Test
    public void failsWithMultipleDefinitionsOfProperty() throws Exception {
        PrimitiveValueNode value = new PrimitiveValueNode("", int.class);
        when(evaluator.evaluate(value)).thenReturn("sth", 13, 'p', 1.23);

        for (String name : new String[] { "prop", "value", "prop", "prop" }) {
            PropertyNode dummy = new PropertyNode(name, value);
            propertyCollector.consume(dummy);
        }
        verify(errorHandler, times(2))
                .error(any(PropertyRedefinitionException.class));
    }

}
