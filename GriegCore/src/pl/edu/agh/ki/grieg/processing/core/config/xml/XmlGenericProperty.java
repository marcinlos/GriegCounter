package pl.edu.agh.ki.grieg.processing.core.config.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

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
        Class<?> clazz = getType();
        try {
            Constructor<?> ctor = clazz.getConstructor(String.class);
            return ctor.newInstance(getString());
        } catch (SecurityException e) {
            throw new ConfigException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigException(e);
        } catch (IllegalArgumentException e) {
            throw new ConfigException(e);
        } catch (InstantiationException e) {
            throw new ConfigException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigException(e);
        } catch (InvocationTargetException e) {
            throw new ConfigException(e);
        }
    }

}
