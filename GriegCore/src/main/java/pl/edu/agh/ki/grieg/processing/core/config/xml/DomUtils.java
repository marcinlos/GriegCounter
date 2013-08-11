package pl.edu.agh.ki.grieg.processing.core.config.xml;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public final class DomUtils {

    private DomUtils() {
        // non-instantiable
    }
    
    public static void printDom(Element dom, Writer writer) {
        PrintWriter printWriter = new PrintWriter(writer);
        printDom(dom, printWriter, 0);
        printWriter.flush();
    }
    
    public static List<String> formatAttributes(Element dom) {
        List<String> parts = Lists.newArrayList();
        NamedNodeMap attributes = dom.getAttributes();
        for (int i = 0; i < attributes.getLength(); ++ i) {
            String name = attributes.item(i).getNodeName();
            String value = attributes.item(i).getNodeValue();
            parts.add(String.format("%s=%s", name, value));
        }
        return parts;
    }
    
    private static void printDom(Element dom, PrintWriter writer, int indent) {
        String pad = Strings.repeat("  ", indent);
        writer.printf("%s%s [", pad, dom.getLocalName());
        
        List<String> parts = formatAttributes(dom);
        writer.println(Joiner.on(", ").join(parts) + "]");
        
        NodeList children = dom.getChildNodes();
        for (int i = 0; i < children.getLength(); ++ i) {
            Node item = children.item(i);
            if (item instanceof Element) {
                Element element = (Element) item;
                printDom(element, writer, indent + 1);
            } else if (item instanceof Text) {
                writer.printf("%stxt \"%s\"\n", pad, item.getTextContent().trim());
            }
        }
    }
    
    public static void printDom(Element dom, OutputStream stream) {
        printDom(dom, new OutputStreamWriter(stream));
    }
    
    public static String domToString(Element dom) {
        StringWriter writer = new StringWriter();
        printDom(dom, writer);
        return writer.toString();
    }

}
