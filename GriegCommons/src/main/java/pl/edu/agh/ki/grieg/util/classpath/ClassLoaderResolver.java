package pl.edu.agh.ki.grieg.util.classpath;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import pl.edu.agh.ki.grieg.util.Resources;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

/**
 * Implementation of {@link ResourceResolver} using java {@link ClassLoader} to
 * retrieve resources from the classpath.
 * 
 * @author los
 */
public class ClassLoaderResolver implements ResourceResolver {

    /** Classloader used to lookup resources */
    private final ClassLoader classLoader;

    /**
     * Creates new {@link ClassLoaderResolver} using current thread's context
     * classloader.
     */
    public ClassLoaderResolver() {
        this(Resources.contextClassLoader());
    }

    /**
     * Creates new {@link ClassLoaderResolver} using specified classloader.
     * 
     * @param classLoader
     *            Classloader used to resolver resource names
     */
    public ClassLoaderResolver(ClassLoader classLoader) {
        this.classLoader = Preconditions.checkNotNull(classLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getResource(String name) {
        return classLoader.getResource(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<URL> getResources(String name) {
        try {
            Enumeration<URL> urls = classLoader.getResources(name);
            return Iterators.forEnumeration(urls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("cl", classLoader)
                .toString();
    }

}
