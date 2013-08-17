package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import static org.junit.Assert.*;

import javax.xml.XMLConstants;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.util.xml.XmlParserBuilder;
import pl.edu.agh.ki.grieg.util.xml.dom.Attribute;
import pl.edu.agh.ki.grieg.util.xml.dom.DomConverter;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class DomConverterTest {

    private static final String NS = "http://some.random/namespace";

    private static XmlParser parser;

    private static Document document;

    private DomConverter converter;

    @BeforeClass
    public static void createParser() throws XmlException {
        parser = new XmlParserBuilder().doNotValidate().create();
        document = parser.parseFromClasspath("xml/dom/example.xml");
    }

    @Before
    public void setup() {
        converter = new DomConverter();
    }

    @Test
    public void canConvertSingleSimpleElement() throws XmlException {
        String xml = "<root/>";
        Document doc = parser.parseString(xml);
        Element actual = converter.convert(doc);
        assertEquals(new Element("root"), actual);
    }

    private Attribute xmlns(String ns) {
        String xmlns = XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        return new Attribute(xmlns, "xmlns").val(ns);
    }

    @Test
    public void canConvertSingleElementWithNS() throws XmlException {
        String xml = String.format("<root xmlns=\"%s\"/>", NS);
        Document doc = parser.parseString(xml);
        Element actual = converter.convert(doc);
        assertEquals(new Element(NS, "root").add(xmlns(NS)), actual);
    }

    @Test
    public void canConvertSingleElementWithValue() throws XmlException {
        String xml = "<root>\n\tHello world\n</root>";
        Document doc = parser.parseString(xml);
        Element actual = converter.convert(doc);
        Element expected = new Element("root").val("Hello world");
        assertEquals(expected, actual);
    }

    @Test
    public void canConvertSingleElementWithAttribute() throws XmlException {
        String xml = "<root class=\"java.lang.String\"/>";
        Document doc = parser.parseString(xml);
        Element actual = converter.convert(doc);

        Attribute attr = new Attribute("class").val("java.lang.String");
        Element expected = new Element("root").add(attr);
        assertEquals(expected, actual);
    }

    @Test
    public void canConvertElementWithChild() throws XmlException {
        String xml = "<root>\n<child>\n\tHello\n</child><child>World</child>" +
                "\n</root>";
        Document doc = parser.parseString(xml);
        Element actual = converter.convert(doc);
        
        Element firstChild = new Element("child").val("Hello");
        Element secondChild = new Element("child").val("World");
        
        Element expected = new Element("root").add(firstChild).add(secondChild);
        assertEquals(expected, actual);
    }
    
    @Test
    public void canConvertBigExample() throws XmlException {
        Element actual = converter.convert(document);

        String l = "https://program.compiler/literals";
        String e = "https://program.compiler/expressions";
        String v = "https://program.compiler/variables";
        String c = "https://program.compiler/control";
        String f = "https://program.compiler/functions";
        String a = "https://program.compiler/attributes";
        String o = "https://program.compiler/options";
        
        String xmlns = XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        
        Element expected = new Element("program")
          .add(new Attribute(xmlns, "xsi").val(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI))
          .add(new Attribute(xmlns, "l").val(l))
          .add(new Attribute(xmlns, "e").val(e))
          .add(new Attribute(xmlns, "v").val(v))
          .add(new Attribute(xmlns, "c").val(c))
          .add(new Attribute(xmlns, "f").val(f))
          .add(new Attribute(xmlns, "a").val(a))
          .add(new Attribute(xmlns, "o").val(o))
            
          .add(new Element(f, "function")
            .add(new Attribute("ret").val("void"))
            .add(new Attribute("name").val("foo"))
            .add(new Attribute("args").val("int,float,float"))
            .add(new Attribute(a, "inline").val("true"))
            .add(new Element(c, "for")
              .add(new Attribute("var").val("int i = 0"))
              .add(new Attribute("cond").val("i %lt; 10"))
              .add(new Attribute("update").val("++ i"))
              .add(new Element(v, "var")
                .add(new Attribute("name").val("tmp"))
                .add(new Element(l, "int").val("666")))
              .add(new Element(c, "if")
                .add(new Attribute("cond").val("i % 2 == 0"))
                .add(new Element(e, "assign")
                  .add(new Attribute("to").val("some_global"))
                  .add(new Element(e, "sum")
                    .add(new Attribute(a, "align").val("8"))
                    .add(new Element(e, "first")
                      .add(new Element(v, "varRef")
                        .add(new Attribute("name").val("tmp"))))
                    .add(new Element(e, "second")
                      .add(new Element(e, "mul")
                        .add(new Element(e, "first")
                          .add(new Element(l, "int").val("13")))
                        .add(new Element(e, "second")
                          .add(new Element(v, "varRef")
                            .add(new Attribute("name").val("some_global")))))))))))
          .add(new Element(o, "flags")
            .add(new Element(o, "flag").val("-foptimize-the-sh**t-out-of-it"))
            .add(new Element(o, "flag").val("-fthe-option"))
            .add(new Element(o, "output")
              .add(new Element(o, "dir").val("bin"))
              .add(new Element(o, "file").val("compiler.exe"))));
        
        assertEquals(expected, actual);
    }

}
