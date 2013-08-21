package pl.edu.agh.ki.grieg.processing.core.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.w3c.dom.Document;

import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.AbstractBootstrap;
import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.Config;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ConfigEvaluator;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ConfigEvaluatorBuilder;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config.xml.ConfigTreeReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineAssemblerReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineElementReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineReader;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PropertyReader;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.Resources;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.XmlParser;
import pl.edu.agh.ki.grieg.util.xml.XmlParserBuilder;
import pl.edu.agh.ki.grieg.util.xml.dom.DomConverter;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

import com.google.common.io.Closeables;

/**
 * Base class for XML-based bootstrap implementation. Handles actual
 * configuration interpretation and dependency creation, derived classes need
 * only provide the configuration content. Can well be used on its own, by the
 * constructor accepting an {@link InputStream} from which to read the config.
 * 
 * <p>
 * Uses {@code config.xsd} schema from the classpath for config validation.
 * 
 * @author los
 */
public class XmlBootstrap extends AbstractBootstrap {

    /** Classpath-relative schema path */
    private static final String SCHEMA = "config.xsd";

    /** Config object */
    private Config config;

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
            ConfigNode node = reader.read(root, null);

            logger().debug("Creating config evaluator");
            ConfigEvaluator evaluator = new ConfigEvaluatorBuilder()
                    .build();

            logger().debug("Evaluating configuration...");
            config = evaluator.evaluate(node);
            logger().debug("Configuration evaluated");
        } catch (XmlException e) {
            throw new ResourceNotFoundException(SCHEMA, e);
        }
    }

    public ConfigTreeReader createConfigReader() {
        logger().debug("Creating XML confiuration tree reader");
        PropertyReader propertyReader = new PropertyReader();
        PipelineReader pipelineReader = new PipelineReader(
                new PipelineElementReader(),
                new PipelineAssemblerReader());
        return new ConfigTreeReader(propertyReader, pipelineReader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepare() throws ConfigException {
        logger().debug("Interpreting configuration");

        chooseFileLoader();
        loadProperties();
        buildAssembler();
    }

    /**
     * Creates the file loader and registers it in the {@link AbstractBootstrap}
     */
    private void chooseFileLoader() {
        logger().info("Using default file loader");
        setLoader(new FileLoader());
    }

    /**
     * Reads and interprets the properties
     */
    private void loadProperties() throws ConfigException {
        logger().trace("Interpreting properties");
        Properties properties = config.buildProperties();
        setProperties(properties);
    }

    /**
     * Creates the pipeline assembler specified by the configuration
     */
    private void buildAssembler() throws ConfigException {
        logger().info("Searching for pipeline assembler");
        PipelineAssembler assembler = config.createAssembler();
        logger().info("Created pipeline assembler");
        setPipelineAssembler(assembler);
    }

}
