package pl.edu.agh.ki.grieg.util.parsing;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Naive implementation of parser factory. Reflectively creates instances of
 * parser classes each time the parser is requested. Which sucks really hard,
 * so: TODO: Make it more efficient
 * 
 * @author los
 */
class DefaultParserFactory implements ParserFactory {

    private final Map<Class<?>, Class<? extends Parser<?>>> parsers;

    public DefaultParserFactory() {
        parsers = Maps.newHashMap();
    }

    <T> void register(Class<T> clazz, Class<? extends Parser<? super T>> parser) {
        parsers.put(clazz, parser);
    }

    @Override
    public <T> Parser<? extends T> getParser(Class<T> clazz)
            throws ParserFactoryException {
        Class<? extends Parser<T>> parserClass = getParserClass(clazz);
        if (parserClass != null) {
            try {
                Parser<? extends T> parser = parserClass.newInstance();
                return parser;
            } catch (InstantiationException e) {
                throw new ParserFactoryException(e);
            } catch (IllegalAccessException e) {
                throw new ParserFactoryException(e);
            }
        } else {
            throw new MissingParserException(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends Parser<T>> getParserClass(Class<T> clazz) {
        return (Class<? extends Parser<T>>) parsers.get(clazz);
    }

}
