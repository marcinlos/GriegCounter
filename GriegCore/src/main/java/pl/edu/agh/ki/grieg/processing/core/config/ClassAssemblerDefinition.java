package pl.edu.agh.ki.grieg.processing.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;

/**
 * Partial implementation of {@link AssemblerDefinition} that creates
 * {@link PipelineAssembler} using the name of the class and arbitrary arguments
 * and reflectively looking up and invoking the appropriate constructor. Class
 * name and constructor arguments are provided by the concrete implementations
 * using protected methods {@link #className()} and {@link #arguments()}.
 * 
 * @author los
 */
public abstract class ClassAssemblerDefinition implements AssemblerDefinition {

    private static final Logger logger = LoggerFactory
            .getLogger(ClassAssemblerDefinition.class);

    /**
     * @return Full qualified name of the class to be used as the pipeline
     *         assembler
     */
    protected abstract String className();

    /**
     * @return Arguments to be passed to the constructor of the class determined
     *         by the {@link #className()}
     */
    protected abstract Object[] arguments();

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Using reflection it invokes appropriate constructor of the class.
     */
    @Override
    public PipelineAssembler createAssembler() throws ConfigException {
        try {
            String name = className();
            logger.info("Using class {} as the pipeline assembler", name);

            logger.info("Creating arguments");
            Object[] args = arguments();
            printArgs(args);

            PipelineAssembler assembler = Reflection.create(name, args);
            logger.info("Instance successfully created: {}", assembler);
            return assembler;
        } catch (ReflectionException e) {
            logger.error("Failed to create an assembler");
            throw new ConfigException(e);
        }
    }

    /**
     * For debugging purpose, prings the instantiation arguments 
     */
    private void printArgs(Object[] args) {
        logger.debug("Using following constructor arguments:");
        for (Object arg : args) {
            logger.debug("    {}", arg);
        }
    }

}
