package pl.edu.agh.ki.grieg.decoder.discovery;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;

/**
 * Parser of configuration files describing audio format parsers.
 * 
 * @author los
 */
class ProviderFileParser {

    /** Optional whitespace sequence */
    private static final Pattern WS = Pattern.compile("\\s*");

    /** Nonempty whiltespace sequence */
    private static final Pattern WS_NONEMPTY = Pattern.compile("\\s+");

    /** Alphanumeric word */
    private static final Pattern WORD = Pattern.compile("\\w+");

    /** Word preceeded by at sign (@) */
    private static final Pattern KEYWORD = Pattern.compile("@\\w+");

    /** Colon surrounded by optional whitespace sequences */
    private static final Pattern WS_COLON = Pattern.compile("\\s*:\\s*");

    /**
     * Name of the java class - nonempty alphanumeric strings separated by dots,
     * non-digit first character
     */
    private static final Pattern CLASS_NAME = Pattern
            .compile("[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*");

    /**
     * Parses content of the specified input stream.
     * 
     * @param in
     *            Input stream containing the confiugration data
     * @return Parsers specified in the configuration
     * @throws ParsingException
     *             If there is an error during reading the data
     */
    public Set<ParserDefinition> parse(InputStream in) throws ParsingException {
        Scanner input = new Scanner(in);
        return new Parser(input).parse();
    }

    /**
     * Parses configuration data specified as string.
     * 
     * @param string
     *            Configuration content
     * @return Parsers specified in this string
     * @throws ParsingException
     *             If there is an error during reading the data
     */
    public Set<ParserDefinition> parse(String string) throws ParsingException {
        byte[] rawData = string.getBytes(Charsets.UTF_8);
        InputStream input = new ByteArrayInputStream(rawData);
        return parse(input);
    }

    /**
     * Auxilary class actually doing the hard work. Maintains the parsing state,
     * reads input from {@link Scanner} specified in the constructor.
     * 
     * @author los
     */
    private static final class Parser {

        /** Input source */
        private final Scanner input;

        /** Parsers read from the configuration so far */
        private final Set<ParserDefinition> parsers = Sets.newLinkedHashSet();

        /**
         * Creates new {@link Parser} using specified {@link Scanner} as the
         * input source.
         * 
         * @param input
         *            Scanner to draw input characters from
         */
        public Parser(Scanner input) {
            this.input = input;
        }

        /**
         * Parses the input specified in the constructor and returns the full
         * list of parser definitions reconstructed form the content.
         * 
         * @return List of parser definitions
         * @throws ParsingException
         *             if there was a problem during file parsing
         */
        public Set<ParserDefinition> parse() throws ParsingException {
            while (hasNextEntry()) {
                getAndSkipColon("@class");
                String className = next(CLASS_NAME, "class name");
                getAndSkipColon("@extensions");

                Set<String> extensions = Sets.newHashSet();
                while (input.hasNext(WORD)) {
                    String extension = next(WORD);
                    extensions.add(extension);
                }
                addDefinition(className, extensions);
            }
            checkEOF();
            return parsers;
        }

        /**
         * Checks if there is nothing more to read in the {@link Scanner} used
         * as the input source. If there is, throws an appropriate exception.
         * 
         * @throws SyntaxException
         *             If there is some uncomsumed input
         */
        private void checkEOF() throws SyntaxException {
            skipWs();
            if (input.hasNextLine()) {
                String line = input.nextLine();
                if (!line.isEmpty()) {
                    throw new SyntaxException(line, "EOF or \"@class\"");
                }
            }
        }

        /**
         * @return {@code true} if there is a keyword (i.e. word starring with @)
         *         ahead, {@code false} otherwise
         */
        private boolean hasNextEntry() {
            skipWs();
            input.useDelimiter(WS_COLON);
            return input.hasNext(KEYWORD);
        }

        /**
         * Skips whitespace characters, if there are some ahead.
         */
        private void skipWs() {
            input.skip(WS);
        }

        /**
         * Reads the next keyword, specified as the argument. If there is no
         * keyword ahead, or the read keyword is different than the expected
         * one, {@link SyntaxException} is thrown.
         * 
         * @param keyword
         *            Expected keyword
         * @throws ParsingException
         *             If there is an error during parsing
         */
        private void getAndSkipColon(String keyword) throws ParsingException {
            skipWs();
            input.useDelimiter(WS_COLON);
            String word = next(KEYWORD, quote(keyword));
            input.skip(WS_COLON);
            input.useDelimiter(WS_NONEMPTY);
            if (!word.equals(keyword)) {
                throw new SyntaxException(word, quote(keyword));
            }
        }

        /**
         * Retrieves next part of the input, assuming it matches the specified
         * pattern. If the input does not match, {@link SyntaxException} is
         * thrown.
         * 
         * @param pattern
         *            Pattern dscribing the token to read
         * @return Lexeme of the read token
         * @throws SyntaxException
         *             If the input does not match the expected pattern
         */
        private String next(Pattern pattern) throws SyntaxException {
            return next(pattern, null);
        }

        /**
         * Retrieves next part of the input, assuming it matches the specified
         * pattern. If the input does not match, {@link SyntaxException} is
         * thrown. Second argument is used as the expected strings category.
         * 
         * @param pattern
         *            Pattern dscribing the token to read
         * @return Lexeme of the read token
         * @throws SyntaxException
         *             If the input does not match the expected pattern
         */
        private String next(Pattern pattern, String expected)
                throws SyntaxException {
            try {
                return input.next(pattern);
            } catch (NoSuchElementException e) {
                throw new SyntaxException(input.nextLine(), expected);
            }
        }

        /**
         * Creates and saves to the internal list new provider definiton.
         * 
         * @param className
         *            Name of the provider clas
         * @param extensions
         *            Common extensions supported by this provider
         */
        private void addDefinition(String className, Set<String> extensions) {
            parsers.add(new ParserDefinition(className, extensions));
        }

        /**
         * Returns string surrounded by ordinary quotes.
         * 
         * @param string
         *            String to quote
         * @return Quoted string
         */
        private static String quote(String string) {
            return String.format("\"%s\"", string);
        }
    }

}
