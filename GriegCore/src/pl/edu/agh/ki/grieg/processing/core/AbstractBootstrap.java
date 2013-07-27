package pl.edu.agh.ki.grieg.processing.core;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.util.Properties;

public abstract class AbstractBootstrap implements Bootstrap {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final AnalyzerConfig config = new AnalyzerConfig();
    
    protected abstract void prepare() throws ConfigException;
    
    protected void setLoader(FileLoader loader) {
        config.setFileLoader(loader);
    }
    
    protected void setProperties(Properties properties) {
        config.setProperties(properties);
    }

    protected void setPipelineAssembler(PipelineAssembler assembler) {
        config.setPipelineAssembler(assembler);
    }
    
    protected Logger logger() {
        return logger;
    }
    
    public final Analyzer createAnalyzer() throws ConfigException {
        logger.info("Initializing the system");
        logger.info("Initializing the dependencies");
        prepare();
        checkNotNull(config.getFileLoader());
        checkNotNull(config.getProperties());
        checkNotNull(config.getPipelineFactory());
        
        logger.info("Dependencies ready, creating the main object");
        return new Analyzer(config);
    }
}
