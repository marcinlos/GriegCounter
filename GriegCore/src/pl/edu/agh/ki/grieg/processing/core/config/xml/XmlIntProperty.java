package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.config.ConversionException;

@XmlType
public class XmlIntProperty extends XmlProperty<Integer> {

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer convert() throws ConversionException {
        try {
            return Integer.valueOf(getString());
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("[int] %s -> %s", getName(), getString());
    }

}
