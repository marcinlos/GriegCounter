package pl.edu.agh.ki.grieg.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Predicate;

/**
 * Predicate checking qualified name equality.
 * 
 * @author los
 */
public class QNameEquals implements Predicate<Node> {

    /** Qualified name of accepted nodes */
    private final QName name;
    
    public QNameEquals(QName name) {
        this.name = checkNotNull(name);
    }
    
    @Override
    public boolean apply(Node e) {
        return name.equals(e.qname());
    }
}
