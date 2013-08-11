package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

/**
 * Creator of the {@link ProcessorFactory} object, providing all the necessary
 * dependencies and configuration. 
 * 
 * @author los
 */
public interface Bootstrap {

    /**
     * Creates new {@link ProcessorFactory}.
     * 
     * @return {@code ProcessorFactory} instance
     * @throws ConfigException
     *             If there is a problem with configuration
     */
    ProcessorFactory createFactory() throws ConfigException;

}
