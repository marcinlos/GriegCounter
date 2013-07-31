package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.ConversionException;

@XmlType
public class XmlLongProperty extends XmlProperty<Long> {
    
    @Override
    public Class<Long> getType() {
        return Long.class;
    }
    
    @Override
    public Long convert() throws ConfigException {
        try {
            return Long.valueOf(getString());
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }
    
    @Override
    public String toString() {
        return String.format("[long] %s -> %s", getName(), safeGetString());
    }
    
}
