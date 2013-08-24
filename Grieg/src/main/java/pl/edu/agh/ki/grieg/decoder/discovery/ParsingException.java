package pl.edu.agh.ki.grieg.decoder.discovery;

public class ParsingException extends ParserDiscoveryException {

    public ParsingException() {
        // empty
    }

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}
