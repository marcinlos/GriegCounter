package pl.edu.agh.ki.grieg.util.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.google.common.base.Splitter;

public class DomPrinter {

    private static final Splitter LINE_SPLITTER = Splitter.on('\n');

    private final PrintWriter pw;

    private DomPrinter(PrintWriter pw) {
        this.pw = pw;
    }

    private String nextPrefix(String prefix, boolean last) {
        return prefix + (last ? "└──" : "├──");
    }

    private String childPrefix(String prefix, boolean last) {
        return prefix + (last ? "   " : "|  ");
    }

    private void visitNode(Node node, String prefix, boolean last) {
        switch (node.getNodeType()) {
        case Node.ELEMENT_NODE:
            visitElement((Element) node, prefix, last);
            break;
        case Node.ATTRIBUTE_NODE:
            visitAttr((Attr) node, prefix, last);
            break;
        case Node.TEXT_NODE:
            visitText((Text) node, prefix, last);
            break;
        default:
            break;
        }
    }

    private void visitElement(Element element, String prefix, boolean last) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        formatNode(element, pw);
        pw.flush();
        Iterable<String> lines = lines(sw.toString());
        printLines(lines, prefix, last);

        boolean noChildren = !element.hasChildNodes();

        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); ++i) {
            boolean lastChild = i == attrs.getLength() - 1 && noChildren;
            String childPrefix = childPrefix(prefix, last);
            visitNode(attrs.item(i), childPrefix, lastChild);
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            boolean lastChild = i == children.getLength() - 1;
            String childPrefix = childPrefix(prefix, last);
            visitNode(children.item(i), childPrefix, lastChild);
        }
    }

    private void visitAttr(Attr attr, String prefix, boolean last) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        formatAttr(attr, pw);
        pw.flush();
        Iterable<String> lines = lines(sw.toString());
        printLines(lines, prefix, last);
    }

    private void visitText(Text text, String prefix, boolean last) {
        String content = text.getTextContent().trim();
        String txt = "Text: \"" + content + "\"";
        printLines(txt, prefix, last);
    }

    private void printLines(String text, String prefix, boolean last) {
        printLines(LINE_SPLITTER.split(text), prefix, last);
    }

    private void printLines(Iterable<String> lines, String prefix, boolean last) {
        String firstLinePrefix = nextPrefix(prefix, last);
        String restPrefix = childPrefix(prefix, last);
        Iterator<String> it = lines.iterator();
        if (it.hasNext()) {
            pw.println(firstLinePrefix + it.next());
            while (it.hasNext()) {
                pw.println(restPrefix + it.next());
            }
        }
    }

    private Iterable<String> lines(String string) {
        return LINE_SPLITTER.split(string);
    }

    private void formatNode(Node node, PrintWriter pw) {
        pw.printf("name: %s\n", node.getNodeName());
        pw.printf("local name: %s\n", node.getLocalName());
        pw.printf("prefix: %s\n", node.getPrefix());
        pw.printf("ns: %s\n", node.getNamespaceURI());
        pw.printf("base uri: %s\n", node.getBaseURI());
        pw.printf("value: %s", node.getNodeValue());
    }

    private void formatAttr(Attr attr, PrintWriter pw) {
        pw.println("+Attribute");
        formatNode(attr, pw);
        pw.println();
        pw.printf("attr name: %s\n", attr.getName());
        pw.printf("attr value: %s\n", attr.getValue());
    }

    public static void print(Element dom, Writer writer) {
        PrintWriter pw = new PrintWriter(writer);
        DomPrinter printer = new DomPrinter(pw);
        printer.visitElement(dom, "", true);
        pw.flush();
    }

    public static void print(Element dom, OutputStream stream) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            print(dom, writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Element dom) {
        StringWriter writer = new StringWriter();
        print(dom, writer);
        return writer.toString();
    }
}
