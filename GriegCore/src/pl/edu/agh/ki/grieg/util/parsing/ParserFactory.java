package pl.edu.agh.ki.grieg.util.parsing;

/**
 * Factory providing parsers converting string to values of arbitrary types.
 * 
 * @author los
 */
public interface ParserFactory {

    <T> Parser<? extends T> getParser(Class<T> clazz)
            throws ParserFactoryException;

}
