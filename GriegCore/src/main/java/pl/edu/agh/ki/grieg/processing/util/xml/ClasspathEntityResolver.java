package pl.edu.agh.ki.grieg.processing.util.xml;

import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import pl.edu.agh.ki.grieg.processing.util.Resources;

/**
 * Entity resolver capable of resolving resources specified with the 'classpath'
 * schema, e.g. {@code classpath:some/path/config.xsd}. As it is completely
 * stateless, it has been made singleton.
 * 
 * @author los
 */
public final class ClasspathEntityResolver implements EntityResolver {

    /** Unique, public instance of {@link ClasspathEntityResolver} */
    public static final EntityResolver INSTANCE = new ClasspathEntityResolver();

    /** Prefix of URIs with classpath schema */
    private static final String CLASSPATH_PREFIX =
            Resources.CLASSPATH_URI_SCHEME + ":";

    /**
     * {@link ClasspathEntityResolver} is not instantiable from the outside code
     */
    private ClasspathEntityResolver() {
        // non-instantiable from the outside
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * If the resource is specified with the {@code classpath} schema, it is
     * searched for on the classpath. Otherwise, {@code null} is returned - this
     * implementation makes no effort to resolve any other kind of addresses.
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        if (systemId.startsWith(CLASSPATH_PREFIX)) {
            String path = systemId.split(":", 2)[1];
            InputStream stream = Resources.asStream(path);
            return stream == null ? null : new InputSource(stream);
        } else {
            return null;
        }
    }
}