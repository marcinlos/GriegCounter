package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Class providing conversion from W3C DOM API to the simplified model from this
 * package.
 * 
 * @author los
 */
public final class DomConverter {

    public DomConverter() {
        // empty
    }

    /**
     * Normalizes the document, and converts the tree to the format implemented
     * in this package.
     * 
     * @param document
     *            Document to convert
     * @return Converted DOM tree
     */
    public Element convert(Document document) {
        document.normalizeDocument();
        return convert(document.getDocumentElement());
    }

    /**
     * Converts the tree rooted in {@code e} to the format implemented in this
     * package.
     * 
     * @param e
     *            Element to convert
     * @return Converted element
     */
    public Element convert(org.w3c.dom.Element e) {
        String ns = e.getNamespaceURI();
        String name = e.getLocalName();
        Element element = new Element(ns, name);

        NamedNodeMap attributes = e.getAttributes();
        for (int i = 0; i < attributes.getLength(); ++i) {
            Attribute attribute = convert((Attr) attributes.item(i));
            element.add(attribute);
        }

        StringBuilder text = new StringBuilder();

        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
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

    /**
     * Converts an attribute to the format implemented in this package.
     * 
     * @param a
     *            Attribute to convert
     * @return Converted attribute
     */
    public Attribute convert(Attr a) {
        String ns = a.getNamespaceURI();
        String name = a.getLocalName();
        String value = a.getValue();
        return new Attribute(ns, name).val(value);
    }

}
