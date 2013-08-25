package pl.edu.agh.ki.grieg.decoder.discovery;

/**
 * Thrown to indicate an error during parsing file containing definitions of
 * audio parsers.
 * 
 * @author los
 */
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
