package pl.edu.agh.ki.grieg.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * Qualified name, consisting of namespace URI and local name.
 *
 * @author los
 */
public final class QName {

    /** Namespace URI */
    private final String ns;

    /** Local name component */
    private final String local;

    /**
     * Creates new qualified name with no namespace URI and specified local
     * name. Name must be non-{@code null}.
     *
     * @param local
     *            Local name
     */
    public QName(String local) {
        this(null, local);
    }

    /**
     * Creates new qualified name with specified namespace URI and local name.
     * Namespace may be {@code null}, in which case {@link QName} represents
     * name with no associated namespace. Local name must be non-{@code null}.
     * 
     * @param ns
     *            Namespace URI
     * @param local
     *            Local name
     */
    public QName(String ns, String local) {
        this.ns = ns;
        this.local = checkNotNull(local);
    }

    /**
     * @return Namespace URI
     */
    public String ns() {
        return ns;
    }

    /**
     * @return Local name
     */
    public String local() {
        return local;
    }

    @Override
    public String toString() {
        return (ns == null ? "" : ns + ":") + local;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof QName) {
            QName other = (QName) o;
            return Objects.equal(ns, other.ns)
                    && Objects.equal(local, other.local);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ns, local);
    }

}
