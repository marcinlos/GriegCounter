package pl.edu.agh.ki.grieg.util.classpath;

/**
 * Interface of URL protocol handler locator. Given URL protocol, searches and
 * returns appropriate handler.
 * 
 * @author los
 */
public interface ProtocolHandlerProvider {

    /**
     * Retrieves handler for particular URL protocol name. Returns {@code null}
     * if the protocol has no associated handler.
     * 
     * @param protocol
     *            Name of the protocol to get handler for
     * @return Handler for the specified protocol, or {@code null} if there is
     *         none
     * @throws ClasspathException
     *             If there is a problem during handler discovery/instantiation
     */
    URLProtocolHandler getHandler(String protocol) throws ClasspathException;

}
