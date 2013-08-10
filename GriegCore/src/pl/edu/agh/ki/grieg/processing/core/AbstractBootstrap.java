package pl.edu.agh.ki.grieg.processing.core;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.util.Properties;

/**
 * Abstract base class facilitating creation of {@link Bootstrap}
 * implementations. Takes care of actual {@link ProcessorFactory} initialization,
 * requires only providing the necessary dependencies. Typical implementation
 * will read and interpret some configuration in the {@link #prepare()} method,
 * and inside it call all the setters, providing the base class with necessary
 * dependencies and values.
 * 
 * @author los
 * 
 */
public abstract class AbstractBootstrap implements Bootstrap {

    /**
     * Logger also for the use of concrete {@link Bootstrap} (nonstatic, will be
     * initialized with the actual, most-derived class descriptor)
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * POJO used to store dependencies and configuration used later to create an
     * {@code ProcessorFactory}
     */
    private final FactoryConfig config = new FactoryConfig();

    /**
     * Empty consstructor
     */
    protected AbstractBootstrap() {
        logger.info("Beginning initialization sequence");
    }

    /**
     * Abstract method to be provided by the concrete {@link Bootstrap}
     * implementation. It is called in the {@link #createFactory()}. The
     * implementation is supposed to provide necessary ingredients of the
     * {@link ProcessorFactory} using protected setters.
     * 
     * @throws ConfigException
     *             If there is a problem with configuration
     */
    protected abstract void prepare() throws ConfigException;

    /**
     * Provides the {@link AbstractBootstrap} with {@link FileLoader} dependency
     * implementation
     * 
     * @param loader
     *            {@link FileLoader} implementation
     */
    protected void setLoader(FileLoader loader) {
        config.setFileLoader(loader);
    }

    /**
     * Provides the {@link AbstractBootstrap} with configuration properties
     * 
     * @param properties
     *            Configuration
     */
    protected void setProperties(Properties properties) {
        config.setProperties(properties);
    }

    /**
     * Provides the {@link AbstractBootstrap} with concrete pipeline assembler
     * 
     * @param assembler
     *            {@link PipelineAssembler} implementation
     */
    protected void setPipelineAssembler(PipelineAssembler assembler) {
        config.setPipelineAssembler(assembler);
    }

    /**
     * @return Logger object
     */
    protected Logger logger() {
        return logger;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Assembles the {@link ProcessorFactory} using dependencies provided by the actual
     * implementation in the {@link #prepare()} method.
     */
    @Override
    public final ProcessorFactory createFactory() throws ConfigException {
        logger.info("Initializing the system");
        logger.info("Initializing the dependencies");
        prepare();
        checkNotNull(config.getFileLoader());
        checkNotNull(config.getProperties());
        checkNotNull(config.getPipelineFactory());

        logger.info("Dependencies ready, creating the main object");
        return new ProcessorFactory(config);
    }
}
