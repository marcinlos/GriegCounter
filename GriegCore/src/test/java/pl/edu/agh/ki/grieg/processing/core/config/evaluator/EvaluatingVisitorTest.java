package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.reflect.TypeToken;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ContentHandler;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ContentHandlerException;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ContentHandlerProvider;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.EvaluatingVisitor;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.NoHandlerException;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ValueException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ValueNode;
import pl.edu.agh.ki.grieg.util.converters.ConversionException;
import pl.edu.agh.ki.grieg.util.converters.Converter;

@RunWith(MockitoJUnitRunner.class)
public class EvaluatingVisitorTest {

    @Mock private Converter converter;
    @Mock private ContentHandlerProvider handlerProvider;
    @Mock private ContentHandler<Object> handler;

    @InjectMocks private EvaluatingVisitor visitor;

    @Before
    public void setup() {
        // invalid due to wildcards :(
        // when(handlerProvider.forQualifier(anyString())).thenReturn(handler);
        doReturn(handler).when(handlerProvider).forQualifier(anyString());
    }

    @Test
    public void canEvalPrimitiveChar() throws Exception {
        ValueNode node = new PrimitiveValueNode("  $", char.class);
        when(converter.convert(eq("  $"), any(TypeToken.class)))
                .thenReturn('$');
        node.accept(visitor);
        assertEquals('$', (char) visitor.getValue(char.class));
    }

    @Test
    public void canEvalPrimitiveByte() throws Exception {
        ValueNode node = new PrimitiveValueNode(" 16", byte.class);
        when(converter.convert(eq(" 16"), any(TypeToken.class)))
                .thenReturn((byte) 16);
        node.accept(visitor);
        assertEquals((byte) 16, (byte) visitor.getValue(byte.class));
    }

    @Test
    public void canEvalPrimitiveShort() throws Exception {
        ValueNode node = new PrimitiveValueNode(" 16", short.class);
        when(converter.convert(eq(" 16"), any(TypeToken.class)))
                .thenReturn((short) 16);
        node.accept(visitor);
        assertEquals((short) 16, (short) visitor.getValue(short.class));
    }

    @Test
    public void canEvalPrimitiveInt() throws Exception {
        ValueNode node = new PrimitiveValueNode(" -16", int.class);
        when(converter.convert(eq(" -16"), any(TypeToken.class)))
                .thenReturn(-16);
        node.accept(visitor);
        assertEquals(-16, (int) visitor.getValue(int.class));
    }

    @Test
    public void canEvalPrimitiveLong() throws Exception {
        ValueNode node = new PrimitiveValueNode("1234", long.class);
        when(converter.convert(eq("1234"), any(TypeToken.class)))
                .thenReturn(1234L);
        node.accept(visitor);
        assertEquals(1234L, (long) visitor.getValue(long.class));
    }

    @Test
    public void canEvalPrimitiveFloat() throws Exception {
        ValueNode node = new PrimitiveValueNode("12.34", float.class);
        when(converter.convert(eq("12.34"), any(TypeToken.class)))
                .thenReturn(12.34f);
        node.accept(visitor);
        assertEquals(12.34f, (float) visitor.getValue(float.class), 1e-6f);
    }

    @Test
    public void canEvalPrimitiveDouble() throws Exception {
        ValueNode node = new PrimitiveValueNode("12.34", double.class);
        when(converter.convert(eq("12.34"), any(TypeToken.class)))
                .thenReturn(12.34);
        node.accept(visitor);
        assertEquals(12.34, (double) visitor.getValue(double.class), 1e-6);
    }

    @Test(expected = ConfigException.class)
    public void failsWhenConversionFails() throws Exception {
        ValueNode node = new PrimitiveValueNode("12.34", double.class);
        when(converter.convert(eq("12.34"), any(TypeToken.class)))
                .thenThrow(new ConversionException());
        node.accept(visitor);
    }

    @Test
    public void canEvalCompleteValueNode() throws Exception {
        Object o = new Object();
        ValueNode node = new CompleteValueNode(o);
        node.accept(visitor);
        assertSame(o, visitor.getValue());
    }

    @Test
    public void canEvalConvertibleNode() throws Exception {
        ValueNode node = new ConvertibleValueNode("file:sth", "java.io.File");
        when(converter.convert(eq("file:sth"), eq(TypeToken.of(File.class))))
                .thenReturn(new File("/some/path"));
        node.accept(visitor);
        assertEquals(new File("/some/path"), visitor.getValue());
    }

    @Test(expected = ValueException.class)
    public void cannotEvalInvalidValue() throws Exception {
        ValueNode node = new ConvertibleValueNode("file:sth", "java.io.File");
        when(converter.convert(eq("file:sth"), eq(TypeToken.of(File.class))))
                .thenThrow(new ConversionException());
        node.accept(visitor);
        assertEquals(new File("/some/path"), visitor.getValue());
    }

    @Test(expected = ValueException.class)
    public void cannotEvalInvalidType() throws Exception {
        ValueNode node = new ConvertibleValueNode("file:sth", "some.#$#$");
        when(converter.convert(eq("file:sth"), any(TypeToken.class)))
                .thenReturn(new File("/some/path"));
        node.accept(visitor);
    }

    @Test
    public void canParseComplexContent() throws Exception {
        ValueNode node = ComplexValueNode.of("complex content", "a");
        doReturn(handler).when(handlerProvider).forQualifier("a");
        when(handler.evaluate("complex content")).thenReturn(666);
        node.accept(visitor);
        assertEquals(666, visitor.getValue());
    }

    @Test(expected = NoHandlerException.class)
    public void cannotParseWithUnknownQualifier() throws Exception {
        ValueNode node = ComplexValueNode.of("complex content", "b");
        doReturn(handler).when(handlerProvider).forQualifier("a");
        doReturn(null).when(handlerProvider).forQualifier("b");
        when(handler.evaluate("complex content")).thenReturn(666);
        node.accept(visitor);
    }

    @Test(expected = ContentHandlerException.class)
    public void failsIfContentHasInvalidType() throws Exception {
        ValueNode node = ComplexValueNode.of("complex content", "a");
        ContentHandler<Thread> throwingHandler = new ContentHandler<Thread>() {
            @Override
            public Object evaluate(Thread content) {
                return "some content";
            }
        };
        doReturn(throwingHandler).when(handlerProvider).forQualifier("a");
        node.accept(visitor);
    }
    
    @Test(expected = ContentHandlerException.class)
    public void failsIfContentHandlerFails() throws Exception {
        ValueNode node = ComplexValueNode.of("complex content", "a");
        when(handler.evaluate(anyObject())).thenThrow(new RuntimeException());
        node.accept(visitor);
    }
    
    @Test
    public void contentHandlerExceptionIsInformative() throws Exception {
        try {
            ValueNode node = ComplexValueNode.of("complex content", "a");
            when(handler.evaluate(anyObject())).thenThrow(new RuntimeException());
            node.accept(visitor);
            fail("Exception should have been thrown");
        } catch (ContentHandlerException e) {
            assertEquals("complex content", e.getContent());
        }
    }

}
