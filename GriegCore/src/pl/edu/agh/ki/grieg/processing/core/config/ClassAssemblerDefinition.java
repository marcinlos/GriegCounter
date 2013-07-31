package pl.edu.agh.ki.grieg.processing.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.util.Reflection;
import pl.edu.agh.ki.grieg.processing.util.ReflectionException;

public abstract class ClassAssemblerDefinition implements AssemblerDefinition {

    private static final Logger logger = LoggerFactory
            .getLogger(ClassAssemblerDefinition.class);

    protected abstract String className();

    protected abstract Object[] arguments();

    @Override
    public PipelineAssembler createAssembler(Context ctx)
            throws ConfigException {
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

    private void printArgs(Object[] args) {
        logger.debug("Using following constructor arguments:");
        for (Object arg : args) {
            logger.debug("    {}", arg);
        }
    }

}
