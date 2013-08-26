package pl.edu.agh.ki.grieg.util.classpath;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

public class ClasspathScanner {

    private final ResourceResolver resolver;

    private final ProtocolHandlerProvider handlers;

    public ClasspathScanner(ResourceResolver resolver,
            ProtocolHandlerProvider handlers) {
        this.resolver = checkNotNull(resolver);
        this.handlers = checkNotNull(handlers);
    }

    public Set<String> getEntries(String base) throws ClasspathException,
            IOException {
        Set<String> files = Sets.newLinkedHashSet();
        Iterator<URL> urls = resolver.getResources(base);
        while (urls.hasNext()) {
            URL url = urls.next();
            System.out.println(url);
            String protocol = url.getProtocol();
            URLProtocolHandler handler = handlers.getHandler(protocol);
            if (handler != null) {
                try {
                    Set<String> newFiles = handler.getFiles(base, url);
                    files.addAll(newFiles);
                } catch (IOException e) {
                    throw new ClasspathException("IO exception", e);
                }
            } else {
                throw new NoProtocolHandler(protocol, url);
            }
        }
        System.out.println("- - - - - - -- - -- - - ");
        return files;
    }

}
