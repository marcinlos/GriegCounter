package pl.edu.agh.ki.grieg.processing.core.config;

import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.AbstractBootstrap;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.Config;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ConfigEvaluator;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ConfigEvaluatorBuilder;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ConfigNode;
import pl.edu.agh.ki.grieg.util.properties.Properties;

/**
 * Abstract base class for {@link Bootstrap} implementations using standard
 * configuration structure described in the
 * {@link pl.edu.agh.ki.grieg.processing.core.config.tree} package. This class
 * handles details of config tree evaluation and transforming results into more
 * usable form.
 * 
 * @author los
 */
public class ConfigBasedBootstrap extends AbstractBootstrap {

    /** Config object */
    protected Config config;

    /**
     * For use of subclasses, to avoid problems with resource cleanup when the
     * ctor throws.
     */
    protected ConfigBasedBootstrap() {
        // empty
    }

    /**
     * Creates fully parsed and processed config from the config tree rooted at
     * {@code node}.
     * 
     * @param node
     *            Root of the config tree
     * @return Config object
     * @throws ConfigException
     *             If there was an error during config processing
     */
    protected Config buildConfig(ConfigNode node) throws ConfigException {
        logger().debug("Creating config evaluator");
        ConfigEvaluator evaluator = new ConfigEvaluatorBuilder()
                .build();

        logger().debug("Evaluating configuration...");
        Config config = evaluator.evaluate(node);
        logger().debug("Configuration evaluated");
        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void prepare() throws ConfigException {
        logger().debug("Interpreting configuration");

        chooseFileLoader();
        loadProperties();
        buildAssembler();
    }

    /**
     * Creates the file loader and registers it in the {@link AbstractBootstrap}
     */
    private void chooseFileLoader() {
        logger().info("Using default file loader");
        setLoader(new FileLoader());
    }

    /**
     * Reads and interprets the properties
     */
    private void loadProperties() throws ConfigException {
        logger().trace("Interpreting properties");
        Properties properties = config.buildProperties();
        setProperties(properties);
    }

    /**
     * Creates the pipeline assembler specified by the configuration
     */
    private void buildAssembler() throws ConfigException {
        logger().info("Searching for pipeline assembler");
        PipelineAssembler assembler = config.createAssembler();
        logger().info("Created pipeline assembler");
        setPipelineAssembler(assembler);
    }

}
