package pl.edu.agh.ki.grieg.decoder.discovery;


public class SyntaxException extends ParsingException {

    private static final String FORMAT_WITH_EXPECTED = 
            "Expected %2$s, got \"%1$s\" instead";

    private static final String FORMAT = "Unexpected \"%s\"";

    private final String expected;

    private final String input;
    
    public SyntaxException(String input) {
        this(input, null);
    }

    public SyntaxException(String input, String expected) {
        super(formatMessage(input, expected));
        this.expected = expected;
        this.input = input;
    }

    public String getExpected() {
        return expected;
    }

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
