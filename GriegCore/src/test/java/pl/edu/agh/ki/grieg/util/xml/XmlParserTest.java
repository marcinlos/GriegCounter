package pl.edu.agh.ki.grieg.util.xml;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.ResourceNotFoundException;
import pl.edu.agh.ki.grieg.processing.core.config.xml.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlSchemaNotFoundException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParseException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlSchemaException;

public class XmlParserTest {

    private XmlParser withSchema;
    
    private XmlParser noSchema;
    
    
    private static InputStream open(String path) {
        return XmlParserTest.class.getResourceAsStream(path);
    }
    
    @Before
    public void setup() throws XmlException {
        withSchema = new XmlParser("xml/simple.xsd");
        noSchema = new XmlParser();
    }
    
    @Test(expected = XmlSchemaNotFoundException.class)
    public void failsWithInvalidSchemaLocation() throws XmlException {
        new XmlParser("bad.xsd");
    }
    
    @Test(expected = XmlSchemaException.class)
    public void failsWithBrokenSchema() throws XmlException {
        new XmlParser("xml/broken.xsd");
    }
    
    @Test
    public void canParseWellFormedDocument() throws XmlException {
        withSchema.parse(open("/xml/valid.xml"));
    }
    
    @Test(expected = XmlParseException.class)
    public void cannotParseDocumentNotConformingToSchema() throws XmlException {
        withSchema.parse(open("/xml/error.xml"));
    }
    
    @Test(expected = XmlParseException.class)
    public void cannotParseGibberishDocument() throws XmlException {
        withSchema.parse(open("/xml/fatal.xml"));
    }
    
    @Test
    public void canParseValidXmlWhenHasNoSchema() throws XmlException {
        noSchema.parse(open("/xml/error.xml"));
    }
}