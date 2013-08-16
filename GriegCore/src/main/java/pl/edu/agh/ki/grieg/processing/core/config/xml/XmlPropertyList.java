package pl.edu.agh.ki.grieg.processing.core.config.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.ConversionException;
import pl.edu.agh.ki.grieg.processing.core.config.PropertiesDefinition;
import pl.edu.agh.ki.grieg.processing.util.xml.DomPrinter;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

import com.google.common.collect.Lists;

@XmlType
public class XmlPropertyList implements PropertiesDefinition {

    private static final Logger logger = LoggerFactory
            .getLogger(XmlPropertyList.class);

    @XmlElements({
        @XmlElement(name = "string", type = XmlStringProperty.class),
        @XmlElement(name = "int", type = XmlIntProperty.class),
        @XmlElement(name = "long", type = XmlLongProperty.class),
        @XmlElement(name = "float", type = XmlFloatProperty.class),
        @XmlElement(name = "value", type = XmlGenericProperty.class) 
    })
    private List<XmlProperty<?>> propertyList = Lists.newArrayList();

    @XmlAnyElement
    private List<Element> custom = Lists.newArrayList();

    @Override
    public Properties buildProperties(Context ctx) throws ConfigException {
        Properties properties = new PropertyMap();
        for (XmlProperty<?> property : propertyList) {
            String name = property.getName();
            Class<?> type = property.getType();
            String text = property.getString();
            logger.trace("PropertyDefinition [{}]: converting \"{}\" to {}",
                    name, text, type);
            try {
                Object value = property.convert();
                properties.put(property.getName(), value);
                logger.trace("    {} -> {}", name, value);
            } catch (ConversionException e) {
                logger.warn("Failed to convert property {}", property);
                throw e;
            } catch (ConfigException e) {
                logger.debug("Failed to process property {}", property);
                throw e;
            }
        }
        for (Element o : custom) {
            String dom = DomPrinter.toString(o);
            logger.trace("Custom element:\n{}", dom);
        }
        return properties;
    }

}
