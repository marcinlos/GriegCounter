package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import pl.edu.agh.ki.grieg.processing.core.config.Property;

@XmlType
public abstract class XmlProperty<T> implements Property<T> {

    @XmlAttribute
    private String name;

    @XmlValue
    private String value;

    @XmlAttribute
    private String valueAttribute;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", name, value);
    }

}
