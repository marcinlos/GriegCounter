package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

/**
 * Creator of the {@link Analyzer} object, providing all the necessary
 * dependencies and configuration.
 * 
 * @author los
 */
public interface Bootstrap {

    /**
     * Creates new {@link Analyzer}
     * 
     * @return {@code Analyzer} instance
     * @throws ConfigException
     *             If there is a problem with configuration
     */
    Analyzer createAnalyzer() throws ConfigException;

}
