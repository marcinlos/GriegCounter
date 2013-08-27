package pl.edu.agh.ki.grieg.util.classpath;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Class providing classpath scanning capabilities.
 * 
 * @author los
 */
public class ClasspathScanner {

    /** Resource resolver used to convert names to URLs */
    private final ResourceResolver resolver;

    /** Repository of protocol handlers */
    private final ProtocolHandlerProvider handlers;

    /**
     * Creates new {@link ClasspathScanner} using specified resource resolver
     * and protocol handlers provider.
     * 
     * @param resolver
     *            Resource resolver used to lookup names
     * @param handlers
     *            Repository of protocol handlers
     */
    public ClasspathScanner(ResourceResolver resolver,
            ProtocolHandlerProvider handlers) {
        this.resolver = checkNotNull(resolver);
        this.handlers = checkNotNull(handlers);
    }

    /**
     * Searches the classpath for files matching the specified pattern. If the
     * {@code base} ends with forward slash ("/"), it is assumed to be a
     * directory, and classpath names all the direct children thereof are
     * returned. Otherwise, it is interpreted as an the ordinary file, and
     * singleton set containing it is returned, provided it is available on the
     * classpath. Otherwise, empty set is returned.
     * 
     * @param baseDir
     *            Directory to search files in
     * @return Set of classpath names of ordinary files in the specified
     *         directory
     * @throws ClasspathException
     *             If there is some error during traversing the classpath
     */
    public Set<String> getEntries(String baseDir) throws ClasspathException {
        Set<String> entries = Sets.newLinkedHashSet();
        Iterator<URL> urls = resolver.getResources(baseDir);
        if (baseDir.endsWith("/")) {
            while (urls.hasNext()) {
                URL url = urls.next();
                Set<String> newEntries = examineLocation(baseDir, url);
                entries.addAll(newEntries);
            }
        } else if (urls.hasNext()) {
            entries.add(baseDir);
        }
        return entries;
    }

    /**
     * Attempts to fully explore given URL and create set of files in the
     * specified directory.
     * 
     * @param base
     *            Base directory to search files in
     * @param url
     *            Location of the base directory in this classpath element
     * @throws ClasspathException
     *             If there is some error during exploring thes URL content
     */
    private Set<String> examineLocation(String base, URL url)
            throws ClasspathException {
        String protocol = url.getProtocol();
        URLProtocolHandler handler = handlers.getHandler(protocol);
        if (handler != null) {
            try {
                return handler.getFiles(base, url);
            } catch (IOException e) {
                throw new ClasspathException("IO exception", e);
            }
        } else {
            throw new NoProtocolHandlerException(protocol, url);
        }
    }

}
