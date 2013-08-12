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

    private XmlParser withPeopleSchema;
    private XmlParser withMathSchema;
    private XmlParser withBothSchemas;
    private XmlParser noSchema;

    private static InputStream open(String path) {
        return Resources.asStream(path);
    }

    @Before
    public void setup() throws XmlException {
        withPeopleSchema = XmlParser.strict("xml/general/people.xsd");
        withMathSchema = XmlParser.strict("xml/general/math.xsd");
        withBothSchemas = XmlParser.strict("xml/general/people.xsd",
                "xml/general/math.xsd");
        noSchema = XmlParser.flexible();
    }

    /*
     * If the location of the schema specified as the argument is invalid (i.e.
     * such schema does not exist), it throws.
     */
    @Test(expected = XmlSchemaNotFoundException.class)
    public void failsWithInvalidSchemaLocation() throws XmlException {
        XmlParser.strict("bad.xsd");
    }

    /*
     * If the schema at the specified location is broken (i.e. is not a valid
     * XML schema), it throws.
     */
    @Test(expected = XmlSchemaException.class)
    public void failsWithBrokenSchema() throws XmlException {
        XmlParser.strict("xml/general/broken.xsd");
    }

    /*
     * Parser with schema can parse document that conforms to this schema and
     * specifies the schema location.
     */
    @Test
    public void withSchemaCanParseDocumentWithSchema() throws XmlException {
        withPeopleSchema.parse(open("xml/general/people-has-schema.xml"));
    }

    /*
     * Parser with math schema can parse document that conforms to this schema
     * and specifies the schema location.
     */
    @Test
    public void withMathSchemaCanParseMathDocumentWithSchema()
            throws XmlException {
        withMathSchema.parse(open("xml/general/math.xml"));
    }

    /*
     * Parser with math schema cannot parse document that specifies his schema
     * location, which is a schema for different namespace.
     */
    @Test(expected = XmlParseException.class)
    public void withMathSchemaCanParsePeopleDocumentWithSchema()
            throws XmlException {
        withMathSchema.parse(open("xml/general/people-has-schema.xml"));
    }

    /*
     * Parser with schema cannot parse document that does not conform to this
     * schema and specifies the schema location.
     */
    @Test(expected = XmlParseException.class)
    public void withSchemaCannotParseWrongDocumentWithSchema()
            throws XmlException {
        withPeopleSchema.parse(open("xml/general/people-error-has-schema.xml"));
    }

    /*
     * Parser with no schema cannot parse document that does not conform to this
     * schema and specifies the schema location.
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseWrongDocumentWithSchema()
            throws XmlException {
        withPeopleSchema.parse(open("xml/general/people-error-has-schema.xml"));
    }

    /*
     * Parser with schema cannot parse complete rubbish
     */
    @Test(expected = XmlParseException.class)
    public void withSchemaCannotParseGibberish() throws XmlException {
        withPeopleSchema.parse(open("xml/general/fatal.xml"));
    }

    /*
     * Parser with no schema cannot parse complete rubbish
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseGibberish() throws XmlException {
        withPeopleSchema.parse(open("xml/general/fatal.xml"));
    }

    /*
     * Parser with no schema can parse document that specifies its schema
     * location, and conforms to it.
     */
    @Test
    public void noSchemaCanParseDocumentWithInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/people-has-schema.xml"));

    }

    /*
     * Parser with no schema cannot parse document that specifies its schema
     * location, but does not conform to it.
     */
    @Test(expected = XmlParseException.class)
    public void cannotParseInvalidDocumentWithInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/people-error-has-schema.xml"));
    }

    /*
     * Parser with no schema cannot parse document that specifies its schema
     * location, but the location is invalid.
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseDocumentWithMissingInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/people-missing-schema.xml"));
    }

    /*
     * Parser with schema can parse document that specifies its schema location,
     * but the location is invalid.
     */
    @Test
    public void withSchemaCanParseDocumentWithMissingInternalSchema()
            throws XmlException {
        withPeopleSchema.parse(open("xml/general/people-missing-schema.xml"));
    }

    /*
     * Parser with schema can parse document that does not specify its schema
     * location.
     */
    @Test
    public void withSchemaCanParseDocumentWithNoInternalSchema()
            throws XmlException {
        withPeopleSchema.parse(open("xml/general/people-no-schema.xml"));
    }

    /*
     * Parser with no schema cannot parse document that does not specify its
     * schema location.
     */
    @Test(expected = XmlParseException.class)
    public void noSchemaCannotParseDocumentWithNoInternalSchema()
            throws XmlException {
        noSchema.parse(open("xml/general/people-no-schema.xml"));
    }

    /*
     * Parser with no schema can parse document containing both namespaces.
     */
    @Test
    public void noSchemaCanParseCombinedWithSchema() throws XmlException {
        noSchema.parse(open("xml/general/both.xml"));
    }

    /*
     * Parser with one schema cannot parse document containing both namespaces.
     */
    @Test(expected = XmlParseException.class)
    public void withSchemaCanNotParseCombined() throws XmlException {
        withPeopleSchema.parse(open("xml/general/both.xml"));
    }

    /*
     * Parser with both schemas can parse the documen.
     */
    @Test
    public void withBothSchemasCanParseCombined() throws XmlException {
        withBothSchemas.parse(open("xml/general/both.xml"));
    }
}
