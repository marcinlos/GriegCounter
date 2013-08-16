package pl.edu.agh.ki.grieg.util.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.ImmutableSet;

import pl.edu.agh.ki.grieg.processing.util.Resources;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParseException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParserBuilder;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlSchemaException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlSchemaNotFoundException;

public class XmlParserTest {

    private XmlParser withPeopleSchema;
    private XmlParser withMathSchema;
    private XmlParser withBothSchemas;
    private XmlParser noSchema;
    private XmlParser nonValidating;

    private InputStream brokenStream;

    private static InputStream open(String path) {
        return Resources.asStream(path);
    }

    @Before
    public void setup() throws XmlException {

        withPeopleSchema = new XmlParserBuilder()
                .useClasspathSchema("xml/general/people.xsd")
                .create();

        withMathSchema = new XmlParserBuilder()
                .useClasspathSchema("xml/general/math.xsd")
                .create();

        withBothSchemas = new XmlParserBuilder()
                .useClasspathSchema("xml/general/people.xsd")
                .useClasspathSchema("xml/general/math.xsd")
                .create();

        noSchema = new XmlParserBuilder()
                .useSchemaLocationHints()
                .withClassPathAwareResolver()
                .create();
        
        nonValidating = new XmlParserBuilder()
                .doNotValidate()
                .create();

        brokenStream = mock(InputStream.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new IOException();
            }
        });
    }

    /*
     * If the location of the schema specified as the argument is invalid (i.e.
     * such schema does not exist), it throws.
     */
    @Test(expected = XmlSchemaNotFoundException.class)
    public void failsWithInvalidSchemaLocation() throws XmlException {
        new XmlParserBuilder()
                .useClasspathSchema("bad.xsd")
                .create();
    }

    /*
     * If the schema at the specified location is broken (i.e. is not a valid
     * XML schema), it throws.
     */
    @Test(expected = XmlSchemaException.class)
    public void failsWithBrokenSchema() throws XmlException {
        new XmlParserBuilder()
                .useClasspathSchema("xml/general/broken.xsd")
                .create();
    }

    /*
     * Parser with no schema throws when given a IO-broken (throwing IOException
     * at every operation) stream
     */
    @Test(expected = XmlException.class)
    public void cannotParseBrokenStream() throws XmlException {
        noSchema.parse(brokenStream);
    }
    
    @Test
    public void schemaResourceNamesAreCorrectlyDetermined() {
        String people = Resources.get("xml/general/people.xsd").toExternalForm();
        String math = Resources.get("xml/general/math.xsd").toExternalForm();
        
        assertEquals(ImmutableSet.of(), nonValidating.getSchemas());
        assertEquals(ImmutableSet.of(), noSchema.getSchemas());
        assertEquals(ImmutableSet.of(people), withPeopleSchema.getSchemas());
        assertEquals(ImmutableSet.of(math), withMathSchema.getSchemas());
        assertEquals(ImmutableSet.of(people, math), withBothSchemas.getSchemas());
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
     * XML document specified by the URL can be parsed correctly.
     */
    @Test
    public void canParsePassingUrl() throws Exception {
        URL url = Resources.get("xml/general/people-has-schema.xml");
        withPeopleSchema.parse(url);
    }
    
    /*
     * XML document specified by the URL that points to nonexistant location.
     */
    @Test(expected = XmlException.class)
    public void cannotParseNonexistantUrl() throws Exception {
        URL url = new URL("file:///some/invalid/url/no/doc/here.xml");
        nonValidating.parse(url);
    }

    /*
     * Invalid XML document specified by the URL cannot be parsed correctly.
     */
    @Test(expected = XmlParseException.class)
    public void cannotParseInvalidPassingUrl() throws Exception {
        URL url = Resources.get("xml/general/people-error-has-schema.xml");
        withPeopleSchema.parse(url);
    }

    /*
     * XML document specified by the URI can be parsed correctly.
     */
    @Test
    public void canParsePassingUri() throws Exception {
        URL url = Resources.get("xml/general/people-has-schema.xml");
        withPeopleSchema.parse(url.toURI());
    }
    
    /*
     * XML document specified by the URI that points to nonexistant location.
     */
    @Test(expected = XmlException.class)
    public void cannotParseNonexistantUri() throws Exception {
        URI uri = new URI("file://localhost:6666/no/such/file/here.xml");
        nonValidating.parse(uri);
    }

    /*
     * Invalid XML document specified by the URI cannot be parsed correctly.
     */
    @Test(expected = XmlParseException.class)
    public void cannotParseInvalidPassingUri() throws Exception {
        URL url = Resources.get("xml/general/people-error-has-schema.xml");
        withPeopleSchema.parse(url.toURI());
    }
    
    @Test
    public void canParseString() throws XmlException {
        nonValidating.parseString("<tag><blah a=\"666\"/></tag>");
    }
    
    @Test(expected = XmlParseException.class)
    public void cannotParseInvalidString() throws XmlException {
        nonValidating.parseString("<some-unfinished");
    }
    
    @Test
    public void nonValidatingCanParseInvalidDocument() throws Exception {
        nonValidating.parse(open("xml/general/people-error-has-schema.xml"));
    }

    /*
     * XML document specified by File object can be parsed correctly.
     */
    @Test
    public void canParsePassingFile() throws Exception {
        URL url = Resources.get("xml/general/people-has-schema.xml");
        withPeopleSchema.parse(new File(url.toURI()));
    }
    
    /*
     * XML document specified by File object pointing to nonexistant location
     * cannot be parsed correctly.
     */
    @Test(expected = XmlException.class)
    public void cannotParseNonexistantFile() throws Exception {
        withPeopleSchema.parse(new File("/no/such/file/for/sure/right.xml"));
    }

    /*
     * Invalid XML document specified by the File object cannot be parsed
     * correctly.
     */
    @Test(expected = XmlParseException.class)
    public void cannotParseInvalidPassingFile() throws Exception {
        URL url = Resources.get("xml/general/people-error-has-schema.xml");
        withPeopleSchema.parse(new File(url.toURI()));
    }

    /*
     * XML document specified by the classpath-relative name can be parsed
     * correctly.
     */
    @Test
    public void canParseFromClasspath() throws Exception {
        withPeopleSchema
                .parseFromClasspath("xml/general/people-has-schema.xml");
    }

    /*
     * Invalid XML document specified by the classpath-relative name cannot be
     * parsed correctly.
     */
    @Test(expected = XmlParseException.class)
    public void cannotParseInvalidFromClasspath() throws Exception {
        withPeopleSchema.parseFromClasspath(
                "xml/general/people-error-has-schema.xml");
    }

    /*
     * Nonexistant XML document cannot be parsed correctly.
     */
    @Test(expected = XmlException.class)
    public void cannotParseNonexistant() throws Exception {
        withPeopleSchema.parseFromClasspath("ghhhhaww.xml");
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
     * Non-validating rarser with no schema can parser well-formed document with
     * nonexistant schema.
     */
    @Test
    public void nonValidatingCanParseDocumentWithMissingInternalSchema()
            throws XmlException {
        XmlParser parser = new XmlParserBuilder().doNotValidate().create();
        parser.parseFromClasspath("xml/general/people-missing-schema.xml");
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
