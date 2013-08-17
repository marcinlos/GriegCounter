package pl.edu.agh.ki.grieg.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Resource resolver implementation created for debugging purposes. It logs
 * details of all the received requests (type, namespace, public/system id, base
 * URI) using standard logger.
 * 
 * <p>
 * As this class is stateless, it has been made singleton.
 * 
 * @author los
 */
final class ChattyResourceResolver implements LSResourceResolver {

    private static final Logger logger = LoggerFactory
            .getLogger(ChattyResourceResolver.class);

    /** Single, publicly available instance */
    public static final ChattyResourceResolver INSTANCE =
            new ChattyResourceResolver();

    /**
     * {@link ChattyResourceResolver} should not be instantiated
     */
    private ChattyResourceResolver() {
        // non-instantiable from the outside
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LSInput resolveResource(String type, String namespaceURI,
            String publicId, String systemId, String baseURI) {

        String fmt = "Resolving:\n    type = {}\n    ns = {}" +
                "\n    publicID = {}\n    systemID = {}\n    base = {}";

        logger.trace(fmt, type, namespaceURI, publicId, systemId, baseURI);
        return null;
    }
}