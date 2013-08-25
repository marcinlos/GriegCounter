package pl.edu.agh.ki.grieg.decoder.discovery;

/**
 * Thrown to indicate an error during the process of loading audio format
 * parsers.
 * 
 * @author los
 */
public class ParserDiscoveryException extends Exception {

	public ParserDiscoveryException() {
		// empty
	}

	public ParserDiscoveryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserDiscoveryException(String message) {
		super(message);
	}

	public ParserDiscoveryException(Throwable cause) {
		super(cause);
	}

}
