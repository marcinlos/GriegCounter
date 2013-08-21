package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

public class XmlConfigException extends ConfigException {

    public XmlConfigException(String message) {
        super(message);
    }

    public XmlConfigException(Throwable cause) {
        super(cause);
    }

    public XmlConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
