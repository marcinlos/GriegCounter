package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public final class DomConverter {

    public DomConverter() {
        // empty
    }
    
    public Element convert(Document document) {
        document.normalizeDocument();
        return convert(document.getDocumentElement());
    }
    
    public Element convert(org.w3c.dom.Element e) {
        String ns = e.getNamespaceURI();
        String name = e.getLocalName();
        Element element = new Element(ns, name);
        
        NamedNodeMap attributes = e.getAttributes();
        for (int i = 0; i < attributes.getLength(); ++ i) {
            Attribute attribute = convert((Attr) attributes.item(i));
            element.add(attribute);
        }
        
        StringBuilder text = new StringBuilder();
        
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); ++ i) {
            org.w3c.dom.Node child = children.item(i);
            if (child instanceof org.w3c.dom.Element) {
                Element childElement = convert((org.w3c.dom.Element) child);
                element.add(childElement);
            } else if (child instanceof Text) {
                text.append(child.getTextContent().trim());
            }
        }
        
        String content = text.toString();
        return element.val(content.isEmpty() ? null : content);
    }
    
    public Attribute convert(Attr a) {
        String ns = a.getNamespaceURI();
        String name = a.getLocalName();
        String value = a.getValue();
        return new Attribute(ns, name).val(value);
    }

}
