package pl.edu.agh.ki.grieg.processing.core.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.w3c.dom.Document;

import pl.edu.agh.ki.grieg.processing.core.config.evaluator.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config.xml.ConfigTreeReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineAssemblerReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineElementReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PropertyReader;
import pl.edu.agh.ki.grieg.util.Resources;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.util.xml.XmlParserBuilder;
import pl.edu.agh.ki.grieg.util.xml.XmlSchemaNotFoundException;
import pl.edu.agh.ki.grieg.util.xml.dom.DomConverter;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

import com.google.common.io.Closeables;

/**
 * Base class for XML-based bootstrap implementation. Handles XML files parsing
 * and delegates configuration interpretation and dependency creation to the
 * parent. Can well be used on its own, by the constructor accepting an
 * {@link InputStream} from which to read the config.
 * 
 * <p>
 * Uses {@code config.xsd} schema from the classpath for config validation.
 * 
 * @author los
 */
public class XmlBootstrap extends ConfigBasedBootstrap {

    /** Classpath-relative schema path */
    private static final String SCHEMA = "config.xsd";

    /**
     * Initializes the bootstrap objet using specified {@link InputStream} as
     * the configuration source.
     * 
     * @param input
     *            {@code InputStream} from which to read the configuration
     * @throws ConfigException
     *             If the configuration cannot be read or interpreted
     */
    public XmlBootstrap(InputStream input) throws ConfigException {
        try {
            init(checkNotNull(input));
        } finally {
            try {
                Closeables.close(input, true);
            } catch (IOException e) {
                throw new AssertionError("Cannot happen");
            }
        }
    }

    /**
     * For use of subclasses, to avoid problems with resource cleanup when the
     * ctor throws.
     */
    protected XmlBootstrap() {
        // empty
    }

    /**
     * Method initializing the bootstrap object. It is extracted from the
     * constructor due to checked exception issues that prevent it from being
     * part of it. Instead, subclasses should invoke it in their constructors.
     * 
     * @param input
     *            {@link InputStream} from which to read configuration
     * @throws ConfigException
     *             If the configuration cannot be read or interpreted
     */
    protected void init(InputStream input) throws ConfigException {
        ConfigNode node = readConfigTree(input);
        config = buildConfig(node);
    }

    private ConfigNode readConfigTree(InputStream input) throws ConfigException {
        try {
            logger().debug("Configuring XML parser");

            logger().info("Loading schema file {} from the classpath", SCHEMA);
            URL url = Resources.get(SCHEMA);
            logger().trace("Schema url: {}", url);

            XmlParser parser = new XmlParserBuilder()
                    .useSchema(url)
                    .create();

            logger().debug("Parsing the configuration document");
            Document doc = parser.parse(input);

            logger().debug("Converting DOM tree to simpler form");
            Element root = new DomConverter().convert(doc);
            ConfigTreeReader reader = createConfigReader();
            return reader.read(root, null);
        } catch (XmlSchemaNotFoundException e) {
            throw new ResourceNotFoundException(SCHEMA, e);
        } catch (XmlException e) {
            throw new XmlConfigException("Error while parsing XML config", e);
        }
    }

    private ConfigTreeReader createConfigReader() {
        logger().debug("Creating XML confiuration tree reader");
        PropertyReader propertyReader = new PropertyReader();
        PipelineReader pipelineReader = new PipelineReader(
                new PipelineElementReader(),
                new PipelineAssemblerReader());
        return new ConfigTreeReader(propertyReader, pipelineReader);
    }

}
