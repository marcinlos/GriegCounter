package pl.edu.agh.ki.grieg.decoder.discovery;

public class MalformedParserDefinitionException extends
        ParserDiscoveryException {

    public MalformedParserDefinitionException() {
        // empty
    }

    public MalformedParserDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedParserDefinitionException(String message) {
        super(message);
    }

    public MalformedParserDefinitionException(Throwable cause) {
        super(cause);
    }

}
