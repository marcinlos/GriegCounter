package pl.edu.agh.ki.grieg.processing.core.config;

import pl.edu.agh.ki.grieg.util.Properties;

public interface PropertiesDefinition {

    Properties buildProperties(Context ctx) throws ConfigException;
    
}
