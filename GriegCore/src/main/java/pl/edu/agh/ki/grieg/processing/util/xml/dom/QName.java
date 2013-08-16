package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public final class QName {
    
    private final String ns;
    
    private final String local;
    
    public QName(String local) {
        this(null, local);
    }

    public QName(String ns, String local) {
        this.ns = ns;
        this.local = checkNotNull(local);
    }
    
    public String ns() {
        return ns;
    }

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
