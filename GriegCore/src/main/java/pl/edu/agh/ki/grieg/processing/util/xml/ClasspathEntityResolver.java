package pl.edu.agh.ki.grieg.processing.util.xml;

import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import pl.edu.agh.ki.grieg.processing.util.Resources;

public class ClasspathEntityResolver implements EntityResolver {

    public static final EntityResolver INSTANCE = new ClasspathEntityResolver();

    private static final String CLASSPATH_PREFIX =
            Resources.CLASSPATH_URI_SCHEME + ":";

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