package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;

@XmlType
public class XmlCustomProperty {
    
    @XmlAnyElement
    private Element dom;

    
    public Element getDom() {
        return dom;
    }

}
