package pl.edu.agh.ki.grieg.processing.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.processing.core.Bootstrap;

import com.google.common.io.Closeables;

/**
 * Implementation of {@link Bootstrap} using XML configuration file from the
 * file system.
 * 
 * @author los
 */
public class XmlFileSystemBootstrap extends XmlBootstrap {

    /**
     * Creates a {@link Bootstrap} object using file at the {@code path} as the
     * configuration source.
     * 
     * @param path
     *            Path of the config file
     */
    public XmlFileSystemBootstrap(String path) throws ConfigException {
        this(new File(path));
    }

    /**
     * Creates a {@link Bootstrap} object using the specified configuration
     * file.
     * 
     * @param file
     *            File to read configuration from
     */
    public XmlFileSystemBootstrap(File file) throws ConfigException {
        logger().info("Using XML configuration from the filesystem");
        InputStream stream = null;
        try {
            logger().info("Using configuration file {}", file);
            stream = new FileInputStream(file);
            init(stream);
        } catch (FileNotFoundException e) {
            throw new ConfigException(e);
        } finally {
            try {
                Closeables.close(stream, true);
            } catch (IOException e) {
                throw new AssertionError("Cannot happen");
            }
        }
    }

}
