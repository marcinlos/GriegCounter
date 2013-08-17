package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;

@XmlType
public class XmlGenericProperty extends XmlProperty<Object> {

    @XmlAttribute(name = "type")
    private String className;

    @Override
    public Class<?> getType() throws ConfigException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ConfigException(e);
        }
    }

    @Override
    public Object convert() throws ConfigException {
        try {
            return Reflection.create(className, getString());
        } catch (ReflectionException e) {
            throw new ConfigException(e);
        }
    }

}
