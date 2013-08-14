package pl.edu.agh.ki.grieg.processing.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.net.URL;

/**
 * Utility class, simplyfing common resource-centered operation, e.g.
 * facilitating simple and expressive access to classpath resources using
 * thread-context classloader.
 * 
 * @author los
 */
public final class Resources {

    /** Prefix of {@code classpath-scnę} */
    public static final String CLASSPATH_URI_SCHEME = "classpath";

    private Resources() {
        // non-instantiable
    }

    /**
     * @return Current context classloader
     */
    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Returns URL obtained from context classloader, or {@code null} if the
     * path cannot be resoved.
     * 
     * @param path
     *            Package-relative path to the resource
     * @return URL of the resource, of {@code null} if it was not found
     */
    public static URL get(String path) {
        checkNotNull(path);
        return contextClassLoader().getResource(path);
    }

    /**
     * Resolves the specified name using current context-classloader, and opens
     * it, if it is found. Otherwise, {@code null} is returned.
     * 
     * @param path
     *            Package-relative path to the resource
     * @return {@link InputStream} for reading the resource, or {@code null}, if
     *         it could not be found
     */
    public static InputStream asStream(String path) {
        checkNotNull(path);
        return contextClassLoader().getResourceAsStream(path);
    }

}
