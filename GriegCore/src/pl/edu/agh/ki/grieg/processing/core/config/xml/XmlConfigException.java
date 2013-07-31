package pl.edu.agh.ki.grieg.processing.core.config.xml;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

public class XmlConfigException extends ConfigException {

    public XmlConfigException() {
        // empty
    }

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
