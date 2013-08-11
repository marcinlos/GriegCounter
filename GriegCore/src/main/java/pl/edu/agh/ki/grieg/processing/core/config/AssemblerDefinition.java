package pl.edu.agh.ki.grieg.processing.core.config;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;

/**
 * Generic interface of a configuration element determining pipeline assembler.
 * There are several ways to specify it, each represented by the implementation
 * of {@link AssemblerDefinition}.
 * 
 * @author los
 */
public interface AssemblerDefinition {

    /**
     * Creates pipeline assembler defined by this configuration element.
     * 
     * @return {@link PipelineAssembler} implementation
     */
    PipelineAssembler createAssembler(Context ctx) throws ConfigException;

}
