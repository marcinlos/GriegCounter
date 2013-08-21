package pl.edu.agh.ki.grieg.processing.core.config2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.reflect.TypeToken;

import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ValueNode;
import pl.edu.agh.ki.grieg.util.converters.Converter;

@RunWith(MockitoJUnitRunner.class)
public class DefaultEvaluatorTest {

    @Mock private Converter converter;
    @Mock private ContentHandlerProvider handlers;
    
    @InjectMocks private DefaultEvaluator evaluator;

    @Test
    public void canEvaluatePrimitiveNodes() throws Exception {
        ValueNode node = new PrimitiveValueNode("3", int.class);
        when(converter.convert(eq("3"), any(TypeToken.class)))
            .thenReturn(3);
        Object value = evaluator.evaluate(node);
        verify(converter).convert(eq("3"), any(TypeToken.class));
        assertEquals(3, value);
    }

}
