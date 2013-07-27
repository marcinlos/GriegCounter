package pl.edu.agh.ki.grieg.util.parsing;


public final class Parsers {

    private static final DefaultParserFactory root = new DefaultParserFactory();

    private Parsers() {
        // Non-instantiable
    }

    public static ParserFactory getFactory() {
        return root;
    }

    public static <T> void register(Class<T> clazz,
            Class<? extends Parser<? super T>> parser) {
        root.register(clazz, parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<T> narrow(Parser<? super T> parser) {
        return (Parser<T>) parser;
    }

}
