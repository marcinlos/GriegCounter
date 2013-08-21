package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import javax.xml.XMLConstants;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ValueNode;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.util.xml.XmlParserBuilder;
import pl.edu.agh.ki.grieg.util.xml.dom.Attribute;
import pl.edu.agh.ki.grieg.util.xml.dom.DomConverter;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class PropertyReaderTest {

    private static final String XMLNS_DECL =
            "xmlns:xsi=\"" + XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI +
                    "\" xmlns=\"" + ConfigTreeReader.NS + "\"";

    private static XmlParser parser;

    private PropertyReader reader;

    @Mock
    private Context ctx;

    @BeforeClass
    public static void createParser() throws XmlException {
        parser = new XmlParserBuilder().doNotValidate().create();
    }

    @Before
    public void setup() {
        reader = new PropertyReader();
    }

    private PropertyNode parse(String xml) throws Exception {
        Document doc = parser.parseString(xml);
        Element e = new DomConverter().convert(doc);
        return reader.read(e, ctx);
    }

    @Test
    public void canParseInt() throws Exception {
        String xml = "<int " + XMLNS_DECL + " name=\"some.prop\">143</int>";

        ValueNode valueNode = new PrimitiveValueNode("143", int.class);
        PropertyNode expected = new PropertyNode("some.prop", valueNode);

        PropertyNode actual = parse(xml);

        assertEquals(expected.getValue(), actual.getValue());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected, actual);
    }

    @Test
    public void canParseFloat() throws Exception {
        String xml = "<float " + XMLNS_DECL + " name=\"nodeName\">1.23</float>";

        ValueNode valueNode = new PrimitiveValueNode("1.23", float.class);
        PropertyNode expected = new PropertyNode("nodeName", valueNode);

        PropertyNode actual = parse(xml);
        assertEquals(expected, actual);
    }

    @Test
    public void canParseString() throws Exception {
        String xml = "<string " + XMLNS_DECL + " name=\"nodeName\">" +
                "  blah</string>";

        ValueNode valueNode = new CompleteValueNode("blah");
        PropertyNode expected = new PropertyNode("nodeName", valueNode);

        PropertyNode actual = parse(xml);
        assertEquals(expected, actual);
    }

    @Test
    public void canParseSimpleCustomType() throws Exception {
        String xml = "<value " + XMLNS_DECL + " name=\"nodeName\" " +
                "type=\"some.Name\">blabla</value>";

        ValueNode valueNode = new ConvertibleValueNode("blabla", "some.Name");
        PropertyNode expected = new PropertyNode("nodeName", valueNode);

        PropertyNode actual = parse(xml);
        assertEquals(expected, actual);
    }

    @Test
    public void canParseComplexCustomType() throws Exception {
        String customNs = "http://some.random/namespace";
        String fmt = "<value xmlns:xsi=\"%s\" xmlns=\"%s\" name=\"bob\">" +
                "<property name=\"\">17</property>" +
                "</value>";

        String xml = String.format(fmt,
                XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, customNs);
        Document doc = parser.parseString(xml);
        Element e = new DomConverter().convert(doc);

        ValueNode valueNode = ComplexValueNode.of(e, customNs);
        PropertyNode expected = new PropertyNode("bob", valueNode);

        PropertyNode actual = reader.read(e, ctx);
        assertEquals(expected, actual);
    }

    @Test(expected = InvalidNodeException.class)
    public void cannotParseGarbage() throws Exception {
        String xml = "<random-garbage " + XMLNS_DECL + " />";
        parse(xml);
    }

    @Test
    public void invalidNodeExceptionIsInformative() throws Exception {
        try {
            String xml = "<random-garbage " + XMLNS_DECL + " />";
            parse(xml);
        } catch (InvalidNodeException e) {
            String XMLNS = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
            String ATTR_XMLNS = XMLConstants.XMLNS_ATTRIBUTE_NS_URI;

            Element node = e.getNode();
            Element xmlNode = new Element(ConfigTreeReader.NS, "random-garbage")
                    .add(new Attribute(ATTR_XMLNS, "xsi").val(XMLNS))
                    .add(new Attribute(ATTR_XMLNS, "xmlns")
                            .val(ConfigTreeReader.NS));
            assertEquals(xmlNode, node);
            assertThat(e.getMessage(), containsString(node.toString()));
        }
    }

}
