package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

public class DefaultAnalyzerBootstrap extends AbstractBootstrap {

    public DefaultAnalyzerBootstrap() {
        logger().info("Using default initializer");
    }
    
    @Override
    protected void prepare() throws ConfigException {
        logger().info("Using default file loader");
        setLoader(new FileLoader());
        
        logger().info("Creating arbitrary configuration");
        Properties config = new PropertyMap();
        config.putInt("resolution", 10000);
        config.putInt("chunk-size", 2048);
        config.putInt("hop-size", 441);
        setProperties(config);
        
        logger().info("Using default pipeline assembler");
        setPipelineAssembler(new DefaultPipelineAssembler());
    }

}
