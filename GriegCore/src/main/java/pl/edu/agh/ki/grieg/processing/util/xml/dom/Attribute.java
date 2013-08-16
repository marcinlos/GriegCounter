package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * Class representing XML attribute. Does not offer any functionality beyond
 * that of {@link Node}, but it's included for clarity and completeness.
 * 
 * @author los
 */
public class Attribute extends Node {

    /**
     * Creates new {@link Attribute} with specified local name and with no
     * namespace. Name must be non-{@code null}. Initial value is an empty
     * string.
     * 
     * @param name
     *            Local name of the attribute
     */
    public Attribute(String name) {
        this(null, name);
        val("");
    }

    /**
     * Creates new {@link Attribute} with specified namespace and local name.
     * Namespace may be {@code null}, name must be non-{@code null}.
     * 
     * @param ns
     *            Namespace URI of this attribute
     * @param name
     *            Local name of this attribute
     */
    public Attribute(String ns, String name) {
        super(ns, name);
    }

    /**
     * Sets the value of this attribute. Value must be non-{@code null}.
     * 
     * @param value
     *            New value of this attribute
     */
    public Attribute val(String value) {
        super.val(checkNotNull(value));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Attribute) {
            Attribute other = (Attribute) o;
            return Objects.equal(qname(), other.qname())
                    && Objects.equal(val(), other.val());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", qname())
                .add("value", val())
                .toString();
    }

}
