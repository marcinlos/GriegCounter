package pl.edu.agh.ki.grieg.util.classpath;

import java.net.URL;

public class NoProtocolHandler extends ClasspathException {

    private static final String FMT_URL =
            "No handler for protocol %s: found while trying to open %s";

    private static final String FMT = "No handler for protocol %s: found";

    private final String protocol;

    private final URL url;

    public NoProtocolHandler(String protocol, URL url) {
        super(formatMessage(protocol, url));
        this.protocol = protocol;
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public URL getURL() {
        return url;
    }

    private static String formatMessage(String protocol, URL url) {
        String fmt = url == null ? FMT_URL : FMT;
        return String.format(fmt, protocol, url);
    }

}
