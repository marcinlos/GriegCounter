package pl.edu.agh.ki.grieg.util.parsing;


/**
 * Thrown when parser for unsupported type is requested from the 
 * {@linkplain ParserFactory}.
 * 
 * @author los
 */
public class MissingParserException extends ParserFactoryException {
    
    private Class<?> clazz;

    public MissingParserException(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * @return {@linkplain Class} for which the parser was requested 
     */
    public Class<?> getRequestedType() {
        return clazz;
    }

}
