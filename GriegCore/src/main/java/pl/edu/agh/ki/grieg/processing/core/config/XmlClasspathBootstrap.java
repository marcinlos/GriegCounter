package pl.edu.agh.ki.grieg.processing.core.config;

import java.io.InputStream;

import pl.edu.agh.ki.grieg.processing.core.Bootstrap;

/**
 * Implementation of {@link Bootstrap} using XML configuration file from the
 * classpath.
 * 
 * @author los
 */
public class XmlClasspathBootstrap extends XmlBootstrap {

    /**
     * Loads the configuration specified by the {@code name} using the thread's
     * context classloader.
     * 
     * @param name
     *            Configuration resource
     */
    public XmlClasspathBootstrap(String name) throws ConfigException {
        super(openStream(name));
    }

    /**
     * Loads the configuration from resource named {@code name} using specified
     * classloader.
     * 
     * @param name
     *            Configuration resource
     * @param classLoader
     *            Classloader to be used to load the configuration
     */
    public XmlClasspathBootstrap(String name, ClassLoader classLoader)
            throws ConfigException {
        super(openStream(name, classLoader));
    }

    /**
     * Opens stream to the specified resource using specified classloader.
     * 
     * @param name
     *            Name of the resource
     * @param classLoader
     *            Classloader used to find this resource
     * @return {@link InputStream} for reading the specified resource
     * @throws ConfigException
     *             If the specified resource could not be found
     */
    private static InputStream openStream(String name, ClassLoader classLoader)
            throws ConfigException {
        InputStream stream = classLoader.getResourceAsStream(name);
        if (stream == null) {
            throw new ConfigFileNotFoundException(name);
        } else {
            return stream;
        }
    }

    /**
     * Opens stream to the specified resource using the thread's context
     * classloader.
     * 
     * @param name
     *            Name of the resource
     * @return {@link InputStream} for reading the specified resource
     * @throws ConfigException
     *             If the specified resource could not be found
     */
    private static InputStream openStream(String name) throws ConfigException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return openStream(name, loader);
    }

}
