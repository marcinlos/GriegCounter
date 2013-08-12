package pl.edu.agh.ki.grieg.processing.util.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import pl.edu.agh.ki.grieg.processing.util.Resources;

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

    private static final String JAXP_SCHEMA_LANGUAGE = 
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    private static final String JAXP_SCHEMA_SOURCE = 
            "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    private static final String W3C_XML_SCHEMA_NS_URI = 
            XMLConstants.W3C_XML_SCHEMA_NS_URI;

    /** DOM parser instance */
    private final DocumentBuilder parser;


    public static XmlParser strict(String... schemas) throws XmlException {
        return new XmlParser(checkNotNull(schemas), true);
    }

    public static XmlParser flexible(String... schemas) throws XmlException {
        return new XmlParser(checkNotNull(schemas), false);
    }
    
    private XmlParser(String[] sources, boolean strict) throws XmlException {
        try {
            parser = createParser(sources, strict);
        } catch (ParserConfigurationException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Creates the document builder.
     */
    private DocumentBuilder createParser(String[] sources, boolean strict)
            throws ParserConfigurationException, XmlException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        if (strict) {
            factory.setValidating(false); // see the javadoc
            Schema schema = createSchema(sources);
            factory.setSchema(schema);
        } else {
            factory.setValidating(true);
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA_NS_URI);
            factory.setAttribute(JAXP_SCHEMA_SOURCE, toUriArray(sources));
        }

        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(ClasspathEntityResolver.INSTANCE);
        builder.setErrorHandler(StrictErrorHandler.INSTANCE);
        return builder;
    }

    private static Source[] toSourceArray(String[] paths) throws XmlException {
        Source[] sources = new Source[paths.length];
        for (int i = 0; i < paths.length; ++i) {
            URL url = Resources.get(paths[i]);
            if (url != null) {
                sources[i] = new StreamSource(url.toExternalForm());
            } else {
                throw new XmlSchemaNotFoundException(paths[i]);
            }
        }
        return sources;
    }
    
    private static String[] toUriArray(String[] paths) throws XmlException {
        String[] uris = new String[paths.length];
        for (int i = 0; i < paths.length; ++ i) {
            URL url = Resources.get(paths[i]);
            if (url != null) {
                uris[i] = url.toString();
            } else {
                throw new XmlSchemaNotFoundException(paths[i]);
            }
        }
        return uris;
    }

    private static Schema createSchema(String[] paths) throws XmlException {
        return createSchema(toSourceArray(paths));
    }

    private static Schema createSchema(Source[] sources) throws XmlException {
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        try {
            Schema schema = factory.newSchema(sources);
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
