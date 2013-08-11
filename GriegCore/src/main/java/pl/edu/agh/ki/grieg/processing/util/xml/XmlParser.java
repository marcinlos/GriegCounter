package pl.edu.agh.ki.grieg.processing.util.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Class providing convinient interface to XML DOM parser. Dealing with DOM
 * parsers in java is a pain due to verbose API throwing diverse range of
 * exceptions. This class is ment as the shortcut, allowing one to validate XML
 * document against the schema and obtain its DOM tree in two simple lines, and
 * only catch one base exception:
 * 
 * <pre>
 * XmlParser parser = new XmlParser(&quot;schema.xsd&quot;);
 * Document doc = parser.parse(&quot;document.xml&quot;);
 * </pre>
 * 
 * @author los
 */
public class XmlParser {

    /** Schema used to validate documents */
    private final Schema schema;

    /** DOM parser instance */
    private final DocumentBuilder parser;

    /**
     * Creates XML parser with no schema, i.e. not validating document structure
     * beyond the XML syntax.
     * 
     * @throws XmlException
     *             If the parser could not be created
     */
    public XmlParser() throws XmlException {
        this((Schema) null, null);
    }

    /**
     * Creates XML parser validating documents using schema specified as the
     * <strong>classpath resource</strong>. Specified {@code schemaPath} cannot
     * be {@code null}.
     * 
     * @param schemaPath
     *            Classpath-relative path to the schema
     * @throws XmlException
     *             If the parser using the specified schema could not be created
     * @throws NullPointerException
     *             If the {@code schemaPath} is {@code null}
     */
    public XmlParser(String schemaPath) throws XmlException {
        this(createSchema(checkNotNull(schemaPath)));
    }

    /**
     * Creates XML parser validating documents using the specified schema.
     * Specified {@code schema} cannot be {@code null}.
     * 
     * @param schema
     *            Schema to validate documents with
     * @throws XmlException
     *             If the parser could not be created
     * @throws NullPointerException
     *             If the {@code cchema} is {@code null}
     */
    public XmlParser(Schema schema) throws XmlException {
        this(checkNotNull(schema), null);
    }

    /**
     * Private auxilary constructor, not checking {@code null}-ity of the
     * specified schema.
     * 
     * @param schema
     *            Schema to be used to validate documents, possibly {@code null}
     * @param dummy
     *            Unused argument, only to differentiate between this
     *            constructor and the public one with just {@link Schema}
     *            argument
     * @throws XmlException
     *             If the parser could not be created
     */
    private XmlParser(Schema schema, Object dummy) throws XmlException {
        try {
            this.schema = schema;
            this.parser = createParser();
        } catch (ParserConfigurationException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Creates the document builder, using schema from the {@link #schema}
     * field.
     */
    private DocumentBuilder createParser() throws ParserConfigurationException,
            XmlSchemaException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setValidating(false); // see the javadoc
        factory.setSchema(schema);

        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(StrictErrorHandler.INSTANCE);
        return builder;
    }

    /**
     * Loads classpath resource using thread's context classloader.
     */
    private static URL getResource(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl.getResource(path);
    }

    /**
     * Creates schema, loads it as the classpath resource.
     */
    private static Schema createSchema(String schemaPath) throws XmlException {
        URL url = getResource(schemaPath);
        if (url == null) {
            throw new XmlSchemaNotFoundException(schemaPath);
        }
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        try {
            Schema schema = factory.newSchema(url);
            return schema;
        } catch (SAXException e) {
            throw new XmlSchemaException(e);
        }
    }

    /**
     * Parses the specified document, validates it and builds its DOM tree.
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
     * Parses the specified document, validates it and builds its DOM tree.
     * 
     * @param input
     *            XML document
     * @return DOM of the specified document
     * @throws XmlParseException
     *             If the document is malformed
     * @throws XmlException
     *             If the DOM could not be created for any reason
     */
    public Document parse(InputSource input) throws XmlException {
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

}
