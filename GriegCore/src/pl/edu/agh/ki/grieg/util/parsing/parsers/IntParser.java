package pl.edu.agh.ki.grieg.util.parsing.parsers;

import pl.edu.agh.ki.grieg.util.parsing.ParseException;
import pl.edu.agh.ki.grieg.util.parsing.Parser;
import pl.edu.agh.ki.grieg.util.parsing.Parsers;

class IntParser implements Parser<Integer> {
    
    static {
        Parsers.register(Integer.class, IntParser.class);
    }

    @Override
    public Integer parse(String value) throws ParseException {
        try {
            return Integer.decode(value);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid integer `" + value + "'", e);
        }
    }

}
