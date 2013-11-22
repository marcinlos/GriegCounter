package pl.edu.agh.ki.grieg.android.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.decoder.mp3.Mp3Parser;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import android.app.Application;

public class GriegApplication extends Application {

    private static final Logger logger = LoggerFactory
            .getLogger(GriegApplication.class);
    
    private ProcessorFactory factory;

    @Override
    public void onCreate() {
        super.onCreate();
        logger.info("Initializing the factory");
        try {
            initFactory();
        } catch (ConfigException e) {
            logger.error("Failed to initialize the processor factory", e);
        }
    }
    
    private void initFactory() throws ConfigException {
        // Bootstrap bootstrap = new XmlClasspathBootstrap("grieg-config.xml");
        Bootstrap bootstrap = new DefaultAndroidBootstrap();
        factory = bootstrap.createFactory();
        String[] rozszerzenia = new String[1];
        rozszerzenia[0] = "mp3";
        factory.getFileLoader().register(new Mp3Parser(), rozszerzenia);
    }
    
    public ProcessorFactory getFactory() {
        return factory;
    }
    
}
