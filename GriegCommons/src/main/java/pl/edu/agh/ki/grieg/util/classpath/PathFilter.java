package pl.edu.agh.ki.grieg.util.classpath;

/**
 * Interface used to filter classpath elements.
 * 
 * @author los
 */
public interface PathFilter {

    /**
     * Determines whether or not the path matches some arbitrary search
     * criteria.
     * 
     * @param path
     *            Path to examine
     * @return {@code true} if the path matches the criteria, {@code false}
     *         otherwise
     */
    boolean matches(String path);

}
