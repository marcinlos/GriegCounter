package pl.edu.agh.ki.grieg.util.classpath;

public class DefaultProtocolHandlerProvider implements ProtocolHandlerProvider {

    @Override
    public URLProtocolHandler getHandler(String protocol) {
        if ("file".equals(protocol)) {
            return new FileProtocolHandler();
        } else {
            return new JarProtocolHandler();
        }
    }

}
