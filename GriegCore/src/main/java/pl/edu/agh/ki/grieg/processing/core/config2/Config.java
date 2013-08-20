package pl.edu.agh.ki.grieg.processing.core.config2;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.PropertiesDefinition;
import pl.edu.agh.ki.grieg.util.Properties;

/**
 * Class representing complete, fully evaluated configuration.
 * 
 * @author los
 */
public class Config implements AssemblerDefinition, PropertiesDefinition {

    /** Definition of pipeline assembler, capable of creating it */
    private final AssemblerDefinition assembler;

    /** Configuration properties */
    private final Properties properties;

    /**
     * Creates new configuration object, using specified assembler definition
     * and properties.
     * 
     * @param assembler
     *            Assembler definition
     * @param properties
     *            Configuration properties
     */
    public Config(AssemblerDefinition assembler, Properties properties) {
        this.assembler = checkNotNull(assembler);
        this.properties = checkNotNull(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PipelineAssembler createAssembler() throws ConfigException {
        return assembler.createAssembler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties buildProperties() {
        return properties;
    }

}
