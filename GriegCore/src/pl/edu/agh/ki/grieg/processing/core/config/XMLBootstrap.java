package pl.edu.agh.ki.grieg.processing.core.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import pl.edu.agh.ki.grieg.processing.core.AbstractBootstrap;
import pl.edu.agh.ki.grieg.processing.core.config.xml.XMLConfig;
import pl.edu.agh.ki.grieg.processing.core.config.xml.XMLProperty;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

import com.google.common.io.Closeables;

public class XMLBootstrap extends AbstractBootstrap {
    
    private static final String SCHEMA = "/config.xsd";
    
    private XMLConfig config; 

    
    public XMLBootstrap(InputStream input) throws ConfigException {
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
     * For use of subclasses, to avoid problems with resource cleanup when
     * the ctor throws
     */
    protected XMLBootstrap() {
        // empty
    }
    
    protected void init(InputStream input) throws ConfigException {
        try {
            logger().debug("Creating JAXB unrmashaller");
            JAXBContext context = JAXBContext.newInstance(XMLConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Schema schema = loadSchema(SCHEMA);
            unmarshaller.setSchema(schema);
            logger().debug("Attempting to parse the XML");
            config = (XMLConfig) unmarshaller.unmarshal(input);
            logger().debug("Sucessfully parsed XML");
        } catch (JAXBException e) {
            throw new ConfigException(e);
        } catch (SAXException e) {
            throw new ConfigException(e);
        }
    }
    
    private Schema loadSchema(String path) throws SAXException {
        logger().info("Loading schema file {} from the classpath", path);
        URL url = getClass().getResource(path);
        logger().trace("Schema url: {}", url);
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        Schema schema = factory.newSchema(url);
        logger().debug("Schema loaded and compiled");
        return schema;
    }
    
    @Override
    protected void prepare() throws ConfigException {
        logger().debug("Interpreting configuration");
        
        logger().trace("Interpreting properties");
        Properties properties = new PropertyMap();
        for (XMLProperty property : config.properties) {
            logger().trace("    {} -> {}", property.name, property.value);
            properties.put(property.name, property.value);
        }
        logger().trace("Finished interpreting properties");
    }

}
