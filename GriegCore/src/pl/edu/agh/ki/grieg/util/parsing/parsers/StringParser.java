package pl.edu.agh.ki.grieg.util.parsing.parsers;

import pl.edu.agh.ki.grieg.util.parsing.ParseException;
import pl.edu.agh.ki.grieg.util.parsing.Parser;
import pl.edu.agh.ki.grieg.util.parsing.Parsers;

class StringParser implements Parser<String> {
    
    static {
        Parsers.register(Double.class, DoubleParser.class);
    }

    @Override
    public String parse(String value) throws ParseException {
        // TODO: Unescape, maybe?
        return value;
    }

}
