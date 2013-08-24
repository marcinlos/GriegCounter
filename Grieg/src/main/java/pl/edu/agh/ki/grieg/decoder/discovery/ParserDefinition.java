package pl.edu.agh.ki.grieg.decoder.discovery;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Immutable structure representing single parser description, consisting of
 * factory class name and list of extensions commonly associated with supported
 * formats.
 * 
 * @author los
 */
final class ParserDefinition {

    /** Name of the factory class */
    private final String className;

    /** Extensions of supported formats */
    private final Set<String> extensions;

    /**
     * Creates new {@link ParserDefinition} from specified factory class and
     * sequence of extensions.
     * 
     * @param className
     *            Name of the factory class
     * @param extensions
     *            Sequence of extensions
     */
    public ParserDefinition(String className, Iterable<String> extensions) {
        this.className = checkNotNull(className);
        this.extensions = ImmutableSet.copyOf(extensions);
    }

    /**
     * @return Name of the factory class
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return Extensions of supported formats
     */
    public Iterable<String> getExtensions() {
        return extensions;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ParserDefinition) {
            ParserDefinition other = (ParserDefinition) o;
            return className.equals(other.className) 
                    && extensions.equals(other.extensions);
        } else {
            return false;
        }
    }

}
