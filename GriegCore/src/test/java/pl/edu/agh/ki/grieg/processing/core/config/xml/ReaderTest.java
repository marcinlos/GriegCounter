package pl.edu.agh.ki.grieg.processing.core.config.xml;

import org.junit.Before;
import org.junit.BeforeClass;
import org.w3c.dom.Document;

import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.util.xml.XmlParserBuilder;
import pl.edu.agh.ki.grieg.util.xml.dom.DomConverter;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class ReaderTest {

    protected static final String CONFIG = "xml/grieg/example-config.xml";
    
    protected static XmlParser parser;
    
    protected Element root;
    protected Element pipeline;

    @BeforeClass
    public static void createParser() throws XmlException {
        parser = new XmlParserBuilder()
                .useClasspathSchema("config.xsd")
                .create();
    }
    
    @Before
    public void parser() throws XmlException {
        Document document = parser.parseFromClasspath(CONFIG);
        root = new DomConverter().convert(document);
        pipeline = root.child("pipeline");
    }
    
}
