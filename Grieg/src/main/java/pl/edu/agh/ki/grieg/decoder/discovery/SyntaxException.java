package pl.edu.agh.ki.grieg.decoder.discovery;

/**
 * Thrown when parsing the file containing definitions of audio format parser is
 * syntactically invalid. Contains information about the invalid input, and
 * optionally about expected content at the point of the error as well.
 * 
 * @author los
 */
public class SyntaxException extends ParsingException {

	/**
	 * String used as the message format when the expected input was provided at
	 * the throw site
	 */
	private static final String FORMAT_WITH_EXPECTED = "Expected %2$s, got \"%1$s\" instead";

	/** Format of the message when the expected input is unavailable */
	private static final String FORMAT = "Unexpected \"%s\"";

	/**
	 * Input that was expected (description or literal content), may be
	 * {@code null}
	 */
	private final String expected;

	/** Unexpected input that caused the error */
	private final String input;

	/**
	 * Creates {@link SyntaxException} with no information about the expected
	 * input.
	 * 
	 * @param input
	 *            Actual invalid input
	 */
	public SyntaxException(String input) {
		this(input, null);
	}

	/**
	 * Creates {@link SyntaxException} with information about the expected
	 * input.
	 * 
	 * @param input
	 *            Actual invalid input
	 * @param expected
	 *            Expected input
	 */
	public SyntaxException(String input, String expected) {
		super(formatMessage(input, expected));
		this.expected = expected;
		this.input = input;
	}

	/**
	 * @return Expected input, may be {@code null}
	 */
	public String getExpected() {
		return expected;
	}

	/**
	 * @return Actual invalid input
	 */
	public String getInput() {
		return input;
	}

	private static String formatMessage(String input, String expected) {
		if (expected != null) {
			return String.format(FORMAT_WITH_EXPECTED, input, expected);
		} else {
			return String.format(FORMAT, input);
		}
	}
}
