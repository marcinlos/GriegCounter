package pl.edu.agh.ki.grieg.android.demo;

import pl.edu.agh.ki.grieg.android.misc.ApkResourceResolver;
import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.DefaultBootstrap;
import pl.edu.agh.ki.grieg.util.classpath.ClassLoaderResolver;
import pl.edu.agh.ki.grieg.util.classpath.ClasspathScanner;
import pl.edu.agh.ki.grieg.util.classpath.DefaultProtocolHandlerProvider;
import pl.edu.agh.ki.grieg.util.classpath.ProtocolHandlerProvider;
import pl.edu.agh.ki.grieg.util.classpath.ResourceResolver;

/**
 * Subclass of {@link DefaultBootstrap}, uses custom classpath scanner to create
 * {@link FileLoader}, due to Android limitations - {@link FileLoader} needs to
 * list contents of a folder, which seems to be impossible using
 * {@link ClassLoader#getResource(String)}, which is how default implementation
 * ({@link ClassLoaderResolver}) works.
 * 
 * @author los
 */
public class DefaultAndroidBootstrap extends DefaultBootstrap {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareFileLoader() {
        logger().info("Using file loader with .apk aware resource resolver");
        ResourceResolver resolver = new ApkResourceResolver();
        ProtocolHandlerProvider handlers = new DefaultProtocolHandlerProvider();
        ClasspathScanner scanner = new ClasspathScanner(resolver, handlers);
        FileLoader loader = new FileLoader(scanner);
        setLoader(loader);
    }

}
