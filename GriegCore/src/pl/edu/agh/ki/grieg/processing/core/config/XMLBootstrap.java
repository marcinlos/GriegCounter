package pl.edu.agh.ki.grieg.processing.core.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pl.edu.agh.ki.grieg.processing.core.AbstractBootstrap;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

import com.google.common.io.Closeables;

public class XMLBootstrap extends AbstractBootstrap {
    
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
            logger().debug("Attempting to parse the XML");
            config = (XMLConfig) unmarshaller.unmarshal(input);
            logger().debug("Sucessfully parsed XML");
        } catch (JAXBException e) {
            throw new ConfigException(e);
        }
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
