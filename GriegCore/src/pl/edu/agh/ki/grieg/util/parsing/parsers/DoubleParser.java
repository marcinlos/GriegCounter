package pl.edu.agh.ki.grieg.util.parsing.parsers;

import pl.edu.agh.ki.grieg.util.parsing.ParseException;
import pl.edu.agh.ki.grieg.util.parsing.Parser;
import pl.edu.agh.ki.grieg.util.parsing.Parsers;

class DoubleParser implements Parser<Double> {

    static {
        Parsers.register(Double.class, DoubleParser.class);
    }

    @Override
    public Double parse(String value) throws ParseException {
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid floating point number '" + value
                    + "'", e);
        }
    }

}
