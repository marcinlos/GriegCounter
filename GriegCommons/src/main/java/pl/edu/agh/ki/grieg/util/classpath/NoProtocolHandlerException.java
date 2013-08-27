package pl.edu.agh.ki.grieg.util.classpath;

import java.net.URL;

/**
 * Exception thrown when during classpath scanning, an element whose URL has
 * protocol with no associated handler is encountered. Carries information about
 * the protocol and, optionally, the URL that caused this exception.
 * 
 * @author los
 */
public class NoProtocolHandlerException extends ClasspathException {

    private static final String FMT_URL =
            "No handler for protocol %s: found while trying to open %s";

    private static final String FMT = "No handler for protocol %s: found";

    /** Protocol with no handler */
    private final String protocol;

    /** URL that caused the exception, may be {@code null} */
    private final URL url;

    /**
     * Creates new {@link NoProtocolHandlerException} with specified protocol
     * and, possibly, URL.
     * 
     * @param protocol
     *            Protocol with no associated handler
     * @param url
     *            URL that caused the exception
     */
    public NoProtocolHandlerException(String protocol, URL url) {
        super(formatMessage(protocol, url));
        this.protocol = protocol;
        this.url = url;
    }

    /**
     * @return Protocol with no associated handler
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return URL that caused the exception
     */
    public URL getURL() {
        return url;
    }

    private static String formatMessage(String protocol, URL url) {
        String fmt = url == null ? FMT_URL : FMT;
        return String.format(fmt, protocol, url);
    }

}
