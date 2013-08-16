package pl.edu.agh.ki.grieg.processing.util.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import pl.edu.agh.ki.grieg.processing.util.Resources;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;

/**
 * Class providing convinient interface to XML DOM parser. Dealing with DOM
 * parsers in java is a pain due to verbose API throwing diverse range of
 * exceptions. This class is ment as the shortcut, allowing one to validate XML
 * document against the schema and obtain its DOM tree in few simple lines, and
 * only catch one base exception:
 * 
 * <pre>
 * XmlParser parser = new XmlParserBuilder()
 *         .useSchema(&quot;schema.xsd&quot;)
 *         .create();
 * Document doc = parser.parse(&quot;document.xml&quot;);
 * </pre>
 * 
 * @author los
 */
public class XmlParser {

    private static final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String JAXP_SCHEMA_SOURCE =
            "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private static final String W3C_XML_SCHEMA_NS =
            XMLConstants.W3C_XML_SCHEMA_NS_URI;

    /** DOM parser instance */
    private final DocumentBuilder parser;

    /** Entity resolver used during parsing and validation */
    private final EntityResolver resolver;

    /** Set of all the schemas used for validation, for comparison/display */
    private final Set<String> schemaSystemIds;

    /**
     * Package-private constructor to hide subtleties of schema identifiers.
     * Used by the {@link XmlParserBuilder}, which sould be used by the client
     * code. Creates new {@link XmlParser} based on the supplied configuration.
     * 
     * @param validating
     *            Whether or not the parser is supposed to validate document's
     *            contents
     * @param schemas
     *            System identifiers of schemas which are to be used for
     *            validation
     * @param resolver
     *            Entity resolver implementation used during parsing
     * @param useHints
     *            Whether or not the parser should use {@code setLocation} hints
     *            while determining the location of a schema for particular
     *            namespace
     * @throws XmlException
     *             If the parser could not be created
     */
    XmlParser(boolean validating, String[] schemas, EntityResolver resolver,
            boolean useHints) throws XmlException {
        this.schemaSystemIds = ImmutableSet.copyOf(schemas);
        this.resolver = resolver;
        this.parser = createParser(validating, schemas, useHints);
    }

    /**
     * Creates the document builder, using the specified schemas.
     * 
     * @param systemIds
     *            System identifiers of the schemas to be used for validation
     * @param useHints
     *            Whether or not the parser should use {@code setLocation} hints
     *            while determining the location of a schema for particular
     *            namespace
     * @return {@link DocumentBuilder} instance
     * @throws ParserConfigurationException
     *             If the java cannot cat
     * @throws XmlException
     */
    private DocumentBuilder createParser(boolean validating,
            String[] systemIds,
            boolean useHints) throws XmlException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);

        if (validating) {
            if (useHints) {
                factory.setValidating(true);
                factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA_NS);
                factory.setAttribute(JAXP_SCHEMA_SOURCE, systemIds);
            } else {
                factory.setValidating(false); // see the javadoc
                Schema schema = createSchema(asSources(systemIds));
                factory.setSchema(schema);
            }
        }
        return createBuilder(factory);
    }

    /**
     * Creates and initializes {@link DocumentBuilder} instance, using
     * previously configured {@link DocumentBuilderFactory}.
     * 
     * @param factory
     *            Document builder factory that shall be used to create
     *            instances of {@code DocumentBuilder}
     * @return New {@code DocumentBuilder} instance
     * @throws XmlException
     *             If there was a problem reading sxml
     */
    private DocumentBuilder createBuilder(DocumentBuilderFactory factory)
            throws XmlException {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(resolver);
            builder.setErrorHandler(StrictErrorHandler.INSTANCE);
            return builder;
        } catch (ParserConfigurationException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Creates schema from the specified schema sources.
     * 
     * @param sources
     *            Sources specifying possibly many different schemas
     * @return
     * @throws XmlException
     */
    private static Schema createSchema(Source[] sources) throws XmlException {
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        // factory.setResourceResolver(ChattyResourceResolver.INSTANCE);
        try {
            Schema schema = factory.newSchema(sources);
            return schema;
        } catch (SAXException e) {
            throw new XmlSchemaException(e);
        }
    }

    /**
     * Loops through the array and wraps system identifiers specified as string
     * into the {@link Source} struct.
     * 
     * @param systemIds
     *            Array of system identifiers
     * @return Array of {@link Source}s
     */
    private static Source[] asSources(String[] systemIds) {
        Source[] sources = new Source[systemIds.length];
        for (int i = 0; i < systemIds.length; ++i) {
            sources[i] = new StreamSource(systemIds[i]);
        }
        return sources;
    }

    /**
     * @return Immutable set of system identifiers of schemas used to validate
     *         XML documents
     */
    public Set<String> getSchemas() {
        return schemaSystemIds;
    }

    /**
     * Parses the document specified as the raw {@link InputStream}. Validates
     * it using specified during the parser's creation or found via
     * {@code schemaLocation} schemas, and builds its DOM tree.
     * 
     * <p>
     * Note: for XML parsing, {@link InputStream} is generally preferred to
     * {@link Reader}, as the former do not interpret the bytes in any way, and
     * so allows the parser to establish encoding on its own.
     * 
     * @param input
     *            XML document
     * @return DOM of the specified document
     * @throws XmlParseException
     *             If the document is malformed
     * @throws XmlException
     *             If the DOM could not be created for any reason
     */
    public Document parse(InputStream input) throws XmlException {
        try {
            return parser.parse(checkNotNull(input));
        } catch (SAXParseException e) {
            throw new XmlParseException(e);
        } catch (SAXException e) {
            throw new XmlException(e);
        } catch (IOException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Parses the document specified as the {@link File} object. Validates it
     * using specified during the parser's creation or found via
     * {@code schemaLocation} schemas, and builds its DOM tree.
     * 
     * @param file
     *            XML document
     * @return DOM of the specified document
     * @throws XmlParseException
     *             If the document is malformed
     * @throws XmlException
     *             If the DOM could not be created for any reason
     */
    public Document parse(File file) throws XmlException {
        try {
            return parser.parse(checkNotNull(file));
        } catch (SAXParseException e) {
            throw new XmlParseException(e);
        } catch (SAXException e) {
            throw new XmlException(e);
        } catch (IOException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Parses the document specified with the URI. Validates it using specified
     * during the parser's creation or found via {@code schemaLocation} schemas,
     * and builds its DOM tree.
     * 
     * @param file
     *            XML document
     * @return DOM of the specified document
     * @throws XmlParseException
     *             If the document is malformed
     * @throws XmlException
     *             If the DOM could not be created for any reason
     */
    public Document parse(URI uri) throws XmlException {
        try {
            return parser.parse(uri.toASCIIString());
        } catch (SAXParseException e) {
            throw new XmlParseException(e);
        } catch (SAXException e) {
            throw new XmlException(e);
        } catch (IOException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Parses the document specified with the URL. Validates it using specified
     * during the parser's creation or found via {@code schemaLocation} schemas,
     * and builds its DOM tree.
     * 
     * @param file
     *            XML document
     * @return DOM of the specified document
     * @throws XmlParseException
     *             If the document is malformed
     * @throws XmlException
     *             If the DOM could not be created for any reason
     */
    public Document parse(URL url) throws XmlException {
        try {
            return parser.parse(url.toExternalForm());
        } catch (SAXParseException e) {
            throw new XmlParseException(e);
        } catch (SAXException e) {
            throw new XmlException(e);
        } catch (IOException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Parses the document using specified classpath-relative path. Validates it
     * using specified during the parser's creation or found via
     * {@code schemaLocation} schemas, and builds its DOM tree.
     * 
     * @param file
     *            XML document
     * @return DOM of the specified document
     * @throws XmlParseException
     *             If the document is malformed
     * @throws XmlException
     *             If the DOM could not be created for any reason
     */
    public Document parseFromClasspath(String resource) throws XmlException {
        URL url = Resources.get(resource);
        if (url != null) {
            return parse(url);
        } else {
            throw new XmlException("Classpath: " + resource);
        }
    }

    /**
     * Parses the document passed as the string. Validates it using specified
     * during the parser's creation or found via {@code schemaLocation} schemas,
     * and builds its DOM tree.
     * 
     * @param xml
     *            String containing the whole XML document
     * @return DOM of the specified document
     * @throws XmlParseException
     *             If the document is malformed
     * @throws XmlException
     *             If the DOM could not be created for any reason
     */
    public Document parseString(String xml) throws XmlException {
        byte[] rawData = xml.getBytes(Charsets.UTF_8);
        InputStream input = new ByteArrayInputStream(rawData);
        return parse(input);
    }

}
