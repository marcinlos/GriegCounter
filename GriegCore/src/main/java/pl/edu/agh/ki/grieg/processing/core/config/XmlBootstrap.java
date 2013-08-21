package pl.edu.agh.ki.grieg.processing.core.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.AbstractBootstrap;
import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config2.Config;
import pl.edu.agh.ki.grieg.processing.core.config2.ConfigEvaluator;
import pl.edu.agh.ki.grieg.processing.core.config2.ConfigEvaluatorBuilder;
import pl.edu.agh.ki.grieg.processing.core.config2.ContentHandler;
import pl.edu.agh.ki.grieg.processing.core.config2.ContentHandlerProvider;
import pl.edu.agh.ki.grieg.processing.core.config2.DefaultEvaluator;
import pl.edu.agh.ki.grieg.processing.core.config2.ErrorCollector;
import pl.edu.agh.ki.grieg.processing.core.config2.PipelineEvaluator;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config2.xml.ConfigReader;
import pl.edu.agh.ki.grieg.processing.core.config2.xml.PipelineAssemblerReader;
import pl.edu.agh.ki.grieg.processing.core.config2.xml.PipelineElementReader;
import pl.edu.agh.ki.grieg.processing.core.config2.xml.PipelineReader;
import pl.edu.agh.ki.grieg.processing.core.config2.xml.PropertyReader;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.Resources;
import pl.edu.agh.ki.grieg.util.converters.Converter;
import pl.edu.agh.ki.grieg.util.converters.ConverterMap;
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

    /** Context kept throughout the configuration interpretation */
    private Context ctx = null;
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
//        try {
//            logger().debug("Creating JAXB unrmashaller");
//            JAXBContext context = JAXBContext.newInstance(XmlConfig.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            Schema schema = loadSchema(SCHEMA);
//            unmarshaller.setSchema(schema);
//            logger().debug("Attempting to parse the XML");
//            config = (XmlConfig) unmarshaller.unmarshal(input);
//            printConfigContent();
//            logger().debug("Sucessfully parsed XML");
//        } catch (JAXBException e) {
//            throw new ConfigException(e);
//        } catch (SAXException e) {
//            throw new ConfigException(e);
//        }
        try {
            XmlParser parser = new XmlParserBuilder()
                    .useClasspathSchema(SCHEMA)
                    .create();
            Element root = new DomConverter().convert(parser.parse(input));
            ConfigReader reader = createReader();
            ConfigNode node = reader.read(root, null);
            
            Converter converter = ConverterMap.newMap();
            ContentHandlerProvider handlers = new ContentHandlerProvider() {
                @Override
                public ContentHandler<?> forQualifier(String qualifier) {
                    return null;
                }
            };

            ConfigEvaluator evaluator = new ConfigEvaluatorBuilder()
                    .setPipelineEvaluator(new PipelineEvaluator())
                    .setErrorHandler(new ErrorCollector())
                    .setEvaluator(new DefaultEvaluator(converter, handlers))
                    .build();
            config = evaluator.evaluate(node);
            
        } catch (XmlException e) {
            throw new ResourceNotFoundException(SCHEMA, e);
        }
    }

    private ConfigReader createReader() {
        PropertyReader propertyReader = new PropertyReader();
        PipelineElementReader elementReader = new PipelineElementReader();
        PipelineAssemblerReader assemblerReader = new PipelineAssemblerReader();
        PipelineReader pipelineReader = new PipelineReader(elementReader, assemblerReader);
        ConfigReader reader = new ConfigReader(propertyReader, pipelineReader);
        return reader;
    }

    private void printConfigContent() {
//        for (Field field : XmlConfig.class.getFields()) {
//            try {
//                Object val = field.get(config);
//                logger().trace("   config.{} = {}", field.getName(), val);
//            } catch (IllegalArgumentException e) {
//                logger().error("Config object probably of invalid type: {}",
//                        config.getClass());
//            } catch (IllegalAccessException e) {
//                logger().trace("Not allowed to read config.{}", field.getName());
//            }
//        }
    }

    /**
     * Loads and compiles the XML schema describing configuration file format.
     *
     * @param path
     *            Classpath-relative path of the schema file
     * @return Compiled {@link Schema} object
     * @throws SAXException
     *             If the schema cannot be correctly parsed
     */
    private Schema loadSchema(String path) throws SAXException {
        logger().info("Loading schema file {} from the classpath", path);
        URL url = Resources.get(path);
        logger().trace("Schema url: {}", url);
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        Schema schema = factory.newSchema(url);
        logger().debug("Schema loaded and compiled");
        return schema;
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
