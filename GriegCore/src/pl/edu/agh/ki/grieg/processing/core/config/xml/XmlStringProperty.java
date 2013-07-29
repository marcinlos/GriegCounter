package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class XmlStringProperty extends XmlProperty<String> {
    
    @Override
    public Class<String> getType() {
        return String.class;
    }
    
    @Override
    public String convert() {
        return getString();
    }
    
    @Override
    public String toString() {
        return String.format("[string] %s -> %s", getName(), getString());
    }
    
}
