package pl.edu.agh.ki.grieg.util.classpath;

public interface ProtocolHandlerProvider {

    /**
     * Retrieves handler for particular URL protocol name. Returns {@code null}
     * if the protocol has no associated handler.
     * 
     * @param protocol
     *            Name of the protocol to get handler for
     * @return Handler for the specified protocol, or {@code null} if there is
     *         none
     */
    URLProtocolHandler getHandler(String protocol);

}
