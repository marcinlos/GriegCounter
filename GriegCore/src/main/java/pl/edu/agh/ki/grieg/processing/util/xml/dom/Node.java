package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * Class representing generic XML document node. Consists of immutable name (
 * {@link QName}), and mutable value.
 * 
 * @author los
 */
public abstract class Node {

    /** Name of the node (namespace + local name) */
    private final QName qname;

    /** Value of the node, interpretation depends on specific node type */
    private String value;

    /**
     * Creates new {@code Node} with specified name. Namespace may be
     * {@code null}, but the local name must be non-{@code null}.
     * 
     * @param ns
     *            Namespace URI
     * @param name
     *            Local name
     */
    public Node(String ns, String name) {
        this.qname = new QName(ns, checkNotNull(name));
    }

    /**
     * @return Namespace URI of this node
     */
    public String ns() {
        return qname.ns();
    }

    /**
     * @return Local name of this node
     */
    public String name() {
        return qname.local();
    }

    /**
     * @return Full qualified name of this node
     * @see #ns()
     * @see #name()
     * @see QName
     */
    public QName qname() {
        return qname;
    }

    /**
     * Sets the node value to the specified string. See {@link #val()} for the
     * precise description of the node value semantics.
     * 
     * @param value
     *            New value of the node
     * @return {@code this}, to enable chained invocations
     */
    public Node val(String value) {
        this.value = value;
        return this;
    }

    /**
     * Returns the value of this node. Exact meaning of the {@code value}
     * property depends on node type:
     * <ul>
     * <li>for {@code Element}s, value is the (trimmed) text contained between
     * opening and closing tags. In case of a mixed content, value is the
     * concatenation of trimmed text parts. E.g. for
     * 
     * <pre>
     * {@code <node>  some <empty/> text </node>}
     * </pre>
     * 
     * node value is {@code sometext}.
     * 
     * <li>for {@code Attribute}s, value is the attribute text, e.g. for
     * attribute {@code class} in {@code <bean class="java.lang.Object"} value
     * is {@code java.lang.Object}.
     * </ul>
     * 
     * @return Value of the node
     */
    public String val() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(qname, value);
    }

}
