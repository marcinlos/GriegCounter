package pl.edu.agh.ki.grieg.android.misc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.util.Resources;
import pl.edu.agh.ki.grieg.util.classpath.ResourceResolver;

import com.google.common.collect.Iterators;

/**
 * Custom implementation of {@link ResourceResolver}. Solves problem caused by
 * the inability to obtain directory URL via Android classloader.
 * 
 * @author los
 */
public class ApkResourceResolver implements ResourceResolver {

    private static final Logger logger = LoggerFactory
            .getLogger(ApkResourceResolver.class);

    /** Jar URL and classpath name separator */
    private static final String JAR_SEPARATOR = "!/";

    /** File containing preprocessed classes, always exists */
    private static final String DEX = "classes.dex";

    /** Location of the application .apk file */
    private static final URL apk;

    static {
        URL dex = Resources.get(DEX);
        try {
            JarURLConnection conn = (JarURLConnection) dex.openConnection();
            apk = conn.getJarFileURL();
            logger.debug("Determined .apk url: {}", apk);
        } catch (IOException e) {
            logger.error("Cannot open classes.dex file");
            throw new Error("Cannot find classes.dex", e);
        }
    }

    /** Classloader used to load ordinary files */
    private final ClassLoader classLoader;

    /**
     * Creates new {@link ApkResourceResolver} using specified classloader for
     * loading ordinary files.
     * 
     * @param classLoader
     *            Classloader to use
     */
    public ApkResourceResolver(ClassLoader classLoader) {
        this.classLoader = checkNotNull(classLoader);
    }

    /**
     * Creates new {@link ApkResourceResolver} using thread context classloader
     * for loading ordinary files.
     */
    public ApkResourceResolver() {
        this(Resources.contextClassLoader());
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
        if (name.endsWith("/")) {
            URL absolute = resolveAgainstApk(DEX /* name */);
            return Iterators.singletonIterator(absolute);
        } else {
            return getResourcesFromClassloader(name);
        }
    }

    /**
     * Creates absolute resource URL from classpath name, by prepending .apk
     * file's URL and {@code !/} characters (used as the separator). Does not
     * perform check if the file actually exists at the resulting URL.
     * 
     * @param name
     *            Classpath name
     * @return Absolute URL
     */
    private URL resolveAgainstApk(String name) {
        try {
            String path = apk + JAR_SEPARATOR + name;
            return new URL("jar", null, path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Resolves specified classpath name using the contained classloader.
     * 
     * @param name
     *            Classpath name of the requested resource
     * @return URLs of the resource
     */
    private Iterator<URL> getResourcesFromClassloader(String name) {
        try {
            Enumeration<URL> urls = classLoader.getResources(name);
            return Iterators.forEnumeration(urls);
        } catch (IOException e) {
            logger.warn("Error while resolving classpath name \"{}\"", name);
            return Iterators.emptyIterator();
        }
    }

}
