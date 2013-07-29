package pl.edu.agh.ki.grieg.processing.core.config.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;

import com.google.common.collect.Lists;

@XmlType
public class XmlPropertyList {

    @XmlElements({
        @XmlElement(name = "string", type = XmlStringProperty.class),
        @XmlElement(name = "int", type = XmlIntProperty.class),
        @XmlElement(name = "long", type = XmlLongProperty.class),
        @XmlElement(name = "float", type = XmlFloatProperty.class),
        @XmlElement(name = "value", type = XmlGenericProperty.class)
    })
    public List<XmlProperty<?>> properties = Lists.newArrayList();
    
    
    @XmlAnyElement
    public List<Element> custom = Lists.newArrayList();

}
