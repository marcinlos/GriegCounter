package pl.edu.agh.ki.grieg.util.classpath;

import java.net.URL;
import java.util.Iterator;

/**
 * Interface providing classpath resource lookup functionality.
 * 
 * @author los
 */
public interface ResourceResolver {

    /**
     * Resolves specified resource name and returns its location. In case the
     * resource cannot be found, returns {@code null}. Resource names ending
     * with forward slash ("/") are interpreted as directory names. In
     * particular, empty string is interpreted as the resource tree root.
     * 
     * @param name
     *            Name of the resource
     * @return Resource location of {@code null} if it cannot be determined
     */
    URL getResource(String name);

    /**
     * Finds all the resources with specified name. If no resources are found,
     * returns empty iterator. Resource names ending with forward slash ("/")
     * are interpreted as directory names. In particular, empty string is
     * interpreted as the resource tree root.
     * 
     * @param name
     *            Name of the resource
     * @return Iterator yielding all the resources with specified name
     */
    Iterator<URL> getResources(String name);

}
