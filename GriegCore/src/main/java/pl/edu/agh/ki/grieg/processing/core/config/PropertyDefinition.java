package pl.edu.agh.ki.grieg.processing.core.config;

/**
 * Configuration element representing strongly typed property with name, type
 * and value.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the property
 */
public interface PropertyDefinition<T> {

    /**
     * @return Name of the property
     */
    String getName();

    /**
     * @return Type of the property
     */
    Class<? extends T> getType() throws ConfigException;

    /**
     * Creates value of the property. Most often that involves parsing some
     * textual representation.
     * 
     * @return Value of the property
     * @throws ConfigException
     */
    T convert() throws ConfigException;

}
