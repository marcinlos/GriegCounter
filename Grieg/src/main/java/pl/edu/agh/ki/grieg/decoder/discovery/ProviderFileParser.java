package pl.edu.agh.ki.grieg.decoder.discovery;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;

public class ProviderFileParser {

    private static final Pattern WS = Pattern.compile("\\s*");
    private static final Pattern WS_NONEMPTY = Pattern.compile("\\s+");
    private static final Pattern WORD = Pattern.compile("\\w+");
    private static final Pattern KEYWORD = Pattern.compile("@\\w+");
    private static final Pattern WS_COLON = Pattern.compile("\\s*:\\s*");

    private static final Pattern CLASS_NAME = Pattern
            .compile("[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*");

    public ProviderFileParser() {
        // TODO Auto-generated constructor stub
    }

    public Set<ParserDefinition> parse(URL url) throws ParsingException {
        return null;
    }

    public Set<ParserDefinition> parse(InputStream in) throws ParsingException {
        Scanner input = new Scanner(in);
        return new Parser(input).parse();
    }

    public Set<ParserDefinition> parse(String string) throws ParsingException {
        byte[] rawData = string.getBytes(Charsets.UTF_8);
        InputStream input = new ByteArrayInputStream(rawData);
        return parse(input);
    }

    private static final class Parser {
        private final Scanner input;
        private final Set<ParserDefinition> parsers = Sets.newLinkedHashSet();

        public Parser(Scanner input) {
            this.input = input;
        }

        public Set<ParserDefinition> parse() throws ParsingException {
            skipWs();
            input.useDelimiter(WS_COLON);
            while (input.hasNext(KEYWORD)) {
                getAndSkipColon("@class");
                String className = next(CLASS_NAME, "class name");
                getAndSkipColon("@extensions");

                Set<String> extensions = Sets.newHashSet();
                while (input.hasNext(WORD)) {
                    String extension = next(WORD);
                    extensions.add(extension);
                }
                addDefinition(className, extensions);
                skipWs();
                input.useDelimiter(WS_COLON);
            }
            checkEOF();
            return parsers;
        }

        private void checkEOF() throws SyntaxException {
            skipWs();
            if (input.hasNextLine()) {
                String line = input.nextLine();
                if (!line.isEmpty()) {
                    throw new SyntaxException(line, "EOF or \"@class\"");
                }
            }
        }

        private void skipWs() {
            input.skip(WS);
        }

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

        private String next(Pattern pattern) throws ParsingException {
            return next(pattern, null);
        }

        private String next(Pattern pattern, String expected)
                throws ParsingException {
            try {
                return input.next(pattern);
            } catch (NoSuchElementException e) {
                throw new SyntaxException(input.nextLine(), expected);
            }
        }

        private void addDefinition(String className, Set<String> extensions) {
            parsers.add(new ParserDefinition(className, extensions));
        }

        private static String quote(String string) {
            return String.format("\"%s\"", string);
        }
    }

}
