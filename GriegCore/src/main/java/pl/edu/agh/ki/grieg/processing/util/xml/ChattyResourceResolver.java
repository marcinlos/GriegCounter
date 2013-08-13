package pl.edu.agh.ki.grieg.processing.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

final class ChattyResourceResolver implements LSResourceResolver {

    private static final Logger logger = LoggerFactory
            .getLogger(ChattyResourceResolver.class);

    @Override
    public LSInput resolveResource(String type, String namespaceURI,
            String publicId, String systemId, String baseURI) {

        String fmt = "Resolving:\n    type = {}\n    ns = {}" +
                "\n    publicID = {}\n    systemID = {}\n    base = {}";

        logger.trace(fmt, type, namespaceURI, publicId, systemId, baseURI);
        return null;
    }
}