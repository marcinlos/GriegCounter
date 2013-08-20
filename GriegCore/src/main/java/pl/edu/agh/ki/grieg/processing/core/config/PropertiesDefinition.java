package pl.edu.agh.ki.grieg.processing.core.config;

import pl.edu.agh.ki.grieg.util.Properties;

/**
 * Configuration element representing the property list. Can build the
 * {@link Properties} object based on its' content.
 * 
 * @author los
 */
public interface PropertiesDefinition {

    /**
     * Creates {@link Properties} object based on the property list it
     * represents.
     * 
     * @return {@code Properties} object containing properties from the list
     * @throws ConfigException
     *             If the configuration element is invalid or cannot be
     *             interpreted
     */
    Properties buildProperties() throws ConfigException;

}
