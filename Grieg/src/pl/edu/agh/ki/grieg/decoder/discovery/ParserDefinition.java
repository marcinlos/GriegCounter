package pl.edu.agh.ki.grieg.decoder.discovery;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Immutable structure representing single parser description, consisting of
 * factory class name and list of extensions commonly associated with supported
 * formats.
 * 
 * @author los
 */
public class ParserDefinition {

    /** Name of the factory class */
    private final String className;

    /** Extensions of supported formats */
    private final List<String> extensions;

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
        this.className = className;
        this.extensions = ImmutableList.copyOf(extensions);
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
    public List<String> getExtensions() {
        return extensions;
    }

}
