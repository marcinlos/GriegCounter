package pl.edu.agh.ki.grieg.util.xml;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.ResourceNotFoundException;
import pl.edu.agh.ki.grieg.processing.core.config.xml.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.util.Resources;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlSchemaNotFoundException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParseException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlSchemaException;

public class XmlParserTest {

    private XmlParser withSchema;

    private XmlParser noSchema;

    private static InputStream open(String path) {
        return Resources.asStream(path);
    }

    @Before
    public void setup() throws XmlException {
        withSchema = new XmlParser("xml/general/simple.xsd");
        noSchema = new XmlParser();
    }

    /*
     * If the location of the schema specified as the argument is invalid (i.e.
     * such schema does not exist), it throws.
     */
    @Test(expected = XmlSchemaNotFoundException.class)
    public void failsWithInvalidSchemaLocation() throws XmlException {
        new XmlParser("bad.xsd");
    }

    /*
     * If the schema at the specified location is broken (i.e. is not a valid
     * XML schema), it throws.
     */
    @Test(expected = XmlSchemaException.class)
    public void failsWithBrokenSchema() throws XmlException {
        new XmlParser("xml/general/broken.xsd");
    }

    /*
     * Parser with schema can parse document that conforms to this schema and
     * specifies the schema location.
     */
    @Test
    public void withSchemaCanParseDocumentWithSchema() throws XmlException {
        withSchema.parse(open("xml/general/has-schema.xml"));
    }

    /*
     * Parser with no schema can parse document that conforms to this schema and
     * specifies the schema location.
     */
    @Test
    public void noSchemaCanParseDocumentWithSchema() throws XmlException {
        noSchema.parse(open("xml/general/has-schema.xml"));
    }

    /*
     * Parser with schema cannot parse document that does not conform to this
     * schema and specifies the schema location.
     */
    @Test(expected = XmlParseException.class)
    public void withSchemaCannotParseWrongDocumentWithSchema()
            throws XmlException {
        withSchema.parse(open("xml/general/error-has-schema.xml"));
    }

    /*
     * Parser with no schema cannot parse document that does not conform to this
     * schema and specifies the schema location.
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseWrongDocumentWithSchema()
            throws XmlException {
        withSchema.parse(open("xml/general/error-has-schema.xml"));
    }

    /*
     * Parser with schema cannot parse complete rubbish
     */
    @Test(expected = XmlParseException.class)
    public void withSchemaCannotParseGibberish() throws XmlException {
        withSchema.parse(open("xml/general/fatal.xml"));
    }

    /*
     * Parser with no schema cannot parse complete rubbish
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseGibberish() throws XmlException {
        withSchema.parse(open("xml/general/fatal.xml"));
    }

    /*
     * Parser with no schema can parse document that specifies its schema
     * location, and conforms to it.
     */
    @Test
    public void noSchemaCanParseDocumentWithInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/has-schema.xml"));
    }

    /*
     * Parser with no schema cannot parse document that specifies its schema
     * location, but does not conform to it.
     */
    @Test(expected = XmlParseException.class)
    public void cannotParseInvalidDocumentWithInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/error-has-schema.xml"));
    }

    /*
     * Parser with no schema cannot parse document that specifies its schema
     * location, but the location is invalid.
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseDocumentWithMissingInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/missing-schema.xml"));
    }

    /*
     * Parser with schema can parse document that specifies its schema location,
     * but the location is invalid.
     */
    @Test
    public void withSchemaCanParseDocumentWithMissingInternalSchema()
            throws XmlException {
        withSchema.parse(open("xml/general/missing-schema.xml"));
    }

    /*
     * Parser with schema can parse document that does not specify its schema
     * location.
     */
    @Test
    public void withSchemaCanParseDocumentWithNoInternalSchema()
            throws XmlException {
        withSchema.parse(open("xml/general/no-schema.xml"));
    }

    /*
     * Parser with no schema cannot parse document that does not specify its
     * schema location.
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseDocumentWithNoInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/no-schema.xml"));
    }
}
