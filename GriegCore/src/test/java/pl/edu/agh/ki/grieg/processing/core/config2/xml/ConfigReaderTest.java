package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static pl.edu.agh.ki.grieg.processing.core.config2.xml.ConfigReader.NS;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Attribute;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class ConfigReaderTest extends ReaderTest {

    @Mock private Context context;
    @Mock private Reader<PropertyNode> propertyReader;
    @Mock private Reader<PipelineNode> pipelineReader;
    
    private ConfigReader reader;

    @Before
    public void setup() throws ConfigException {
        reader = new ConfigReader(propertyReader, pipelineReader);
        
        when(pipelineReader.read(any(Element.class), eq(context)))
                .thenReturn(new PipelineNode());
    }
    
    @Test
    public void canParseEmptyConfigWithNoProperties() throws Exception {
        Element e = new Element(NS, "root").add(new Element(NS, "pipeline"));
        
        ConfigNode config = reader.read(e, context);
        assertThat(config.getPropertyNodes(), empty());
        assertThat(config.getPipelineNodes().getElements(), empty());
        verify(pipelineReader).read(any(Element.class), eq(context));
        verifyNoMoreInteractions(pipelineReader, propertyReader);
    }

    @Test
    public void canParseEmptyConfig() throws Exception {
        Element e = new Element(NS, "root")
                .add(new Element(NS, "properties"))
                .add(new Element(NS, "pipeline"));
        ConfigNode config = reader.read(e, context);
        assertThat(config.getPropertyNodes(), empty());
        assertThat(config.getPipelineNodes().getElements(), empty());
        verify(pipelineReader).read(any(Element.class), eq(context));
        verifyNoMoreInteractions(pipelineReader, propertyReader);
    }

    @Test
    public void canParseWithSingleProperty() throws Exception {
        Element properties = new Element(NS, "properties");
        Element pipeline = new Element(NS, "pipeline");
        Element e = new Element(NS, "root").add(properties).add(pipeline);

        Element prop = new Element(NS, "int")
                .add(new Attribute("name").val("someProp"))
                .val("1234");
        properties.add(prop);

        PrimitiveValueNode value = new PrimitiveValueNode("1234", int.class);
        PropertyNode propertyNode = new PropertyNode("someProp", value);
        List<PropertyNode> map = ImmutableList.<PropertyNode> builder()
                .add(propertyNode)
                .build();
        when(propertyReader.read(prop, context)).thenReturn(propertyNode);

        ConfigNode config = reader.read(e, context);
        ConfigNode expected = new ConfigNode(map, new PipelineNode());
        assertEquals(expected, config);
    }

}
