package pl.edu.agh.ki.grieg.processing.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.Closeables;

public class XMLFileSystemBootstrap extends XMLBootstrap {

    public XMLFileSystemBootstrap(String path) throws ConfigException {
        this(new File(path));
    }

    public XMLFileSystemBootstrap(File file) throws ConfigException {
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
