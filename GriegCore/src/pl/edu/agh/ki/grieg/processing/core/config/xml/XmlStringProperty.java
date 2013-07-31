package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

@XmlType
public class XmlStringProperty extends XmlProperty<String> {
    
    @Override
    public Class<String> getType() {
        return String.class;
    }
    
    @Override
    public String convert() throws ConfigException {
        return getString();
    }
    
    @Override
    public String toString() {
        return String.format("[string] %s -> %s", getName(), safeGetString());
    }
    
}
