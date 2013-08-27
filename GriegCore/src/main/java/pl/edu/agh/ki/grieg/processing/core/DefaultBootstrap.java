package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.util.properties.Properties;
import pl.edu.agh.ki.grieg.util.properties.PropertyMap;

/**
 * Default, example implementation of {@link Bootstrap}.
 * 
 * @author los
 */
public class DefaultBootstrap extends AbstractBootstrap {

    /**
     * Creates new {@link DefaultBootstrap}.
     */
    public DefaultBootstrap() {
        logger().info("Using default initializer");
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Separates preparation into 3 independent steps:
     * <ul>
     * <li>preparing file loader
     * <li>preparing config properties
     * <li>preparing pipeline assembler
     * </ul>
     * 
     * Subclasses can override methods associated with these steps to customize
     * the process.
     */
    @Override
    protected final void prepare() throws ConfigException {
        prepareFileLoader();
        prepareConfig();
        preparePipelineAssembler();
    }

    /**
     * Creates pipeline assembler.
     */
    protected void preparePipelineAssembler() {
        logger().info("Using default pipeline assembler");
        setPipelineAssembler(new DefaultPipelineAssembler());
    }

    /**
     * Creates configuration properties.
     */
    protected void prepareConfig() {
        logger().info("Creating arbitrary configuration");
        Properties config = new PropertyMap();
        config.putInt("resolution", 10000);
        config.putInt("chunk-size", 2048);
        config.putInt("hop-size", 441);
        setProperties(config);
    }

    /**
     * Creating file loader.
     */
    protected void prepareFileLoader() {
        logger().info("Using default file loader");
        setLoader(new FileLoader());
    }

}
