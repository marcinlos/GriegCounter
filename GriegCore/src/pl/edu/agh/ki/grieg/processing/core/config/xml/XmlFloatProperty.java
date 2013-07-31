package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.ConversionException;

@XmlType
public class XmlFloatProperty extends XmlProperty<Float> {

    @Override
    public Class<Float> getType() {
        return Float.class;
    }

    @Override
    public Float convert() throws ConfigException {
        try {
            return Float.valueOf(getString());
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("[int] %s -> %s", getName(), safeGetString());
    }

}
