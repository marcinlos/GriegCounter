package pl.edu.agh.ki.grieg.processing.core.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType
public class XMLProperty {
    
    @XmlAttribute
    public String name;
    
    @XmlValue
    public String value;
    
}
