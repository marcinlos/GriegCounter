package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "property")
public class XMLProperty {
    
    @XmlAttribute
    public String name;
    
    @XmlValue
    public String value;
    
}
