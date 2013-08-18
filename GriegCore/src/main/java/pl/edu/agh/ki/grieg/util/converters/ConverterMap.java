package pl.edu.agh.ki.grieg.util.converters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

/**
 * {@link Converter} container, aggregating multiple {@link Converter}s and
 * providing simple {@link Converter} interface.
 * 
 * <p>
 * {@link ConverterMap} maintains a collection of entries consisting of a
 * converter instance and a {@link Predicate} describing supported types. It
 * comes with following predefined converter entries:
 * <ul>
 * <li>entries for primitive types ({@code byte}, {@code char}, {@code short}
 * {@code int}, {@code long}, {@code float}, {@code double}
 * </ul>
 * trivial converter for {@code String}
 * 
 * @author los
 */
public final class ConverterMap implements Converter {

    private static final Logger logger = LoggerFactory
            .getLogger(ConverterMap.class);

    /**
     * Wrapper for static method, provides conversion by invoking it with the
     * specified string. Wraps {@link ReflectionException}s thrown by the
     * invocation in generic {@link ConversionException}.
     */
    private static final class MethodWrapper implements Converter {

        /** Type of the conversion result */
        private final Class<?> primitive;

        /** Static method implementing the conversion functionality */
        private final Method method;

        /**
         * Creates {@link Converter} for specified type using specified static
         * method to realize it.
         * 
         * @param type
         *            Type of the conversion result
         * @param method
         *            Static method providing conversion
         */
        public MethodWrapper(Class<?> type, Method method) {
            this.primitive = type;
            this.method = method;
        }

        /**
         * {@inheritDoc}
         * 
         */
        @Override
        public Object convert(String literal, TypeToken<?> targetType)
                throws ConversionException {
            try {
                return Reflection.invokeStatic(method, literal.trim());
            } catch (ReflectionException e) {
                throw new ConversionException(e);
            }
        }

        @Override
        public String toString() {
            return String.format("Converter[%s]", primitive.getName());
        }
    }

    /** Collection of registered converter entries */
    private final Set<ConverterEntry> entries = Sets.newLinkedHashSet();

    /**
     * Creates new {@link ConverterMap} with standard default converter entries.
     * 
     * @return New {@link ConverterMap} instance
     */
    public static ConverterMap newMap() {
        ConverterMap map = newEmptyMap();
        map.registerBuiltins();
        return map;
    }

    /**
     * Creates new {@link ConverterMap} without default entries. Returned
     * instance has no converter entries whatsoever, for each conversion request
     * it shall throw {@link NoMatchingConverterException}.
     * 
     * @return New empty {@link ConverterMap} instance
     */
    public static ConverterMap newEmptyMap() {
        return new ConverterMap();
    }

    /**
     * Creates new empty {@link ConverterMap}
     */
    private ConverterMap() {
        // empty
    }

    /**
     * Creates default converter entries in the internal structures of the map.
     */
    private void registerBuiltins() {
        registerPrimitives(byte.class, short.class, int.class, long.class,
                float.class, double.class);

        register(Types.sameAs(String.class), new Converter() {
            @Override
            public Object convert(String literal, TypeToken<?> targetType)
                    throws ConversionException {
                return literal;
            }
        });

        register(Types.sameAs(char.class), new CharConverter());
    }

    /**
     * Creates converters for specified primitive types, and registers them.
     * 
     * @param primitives
     *            Classes to create entries for
     */
    private void registerPrimitives(Class<?>... primitives) {
        for (Class<?> clazz : primitives) {
            try {
                Converter converter = forPrimitive(clazz);
                register(clazz, converter);
            } catch (ReflectionException e) {
                logger.warn("Cannot register converter for " + clazz.getName()
                        + " due to:", e);
            }
        }
    }

    /**
     * Creates a converter for primitive type, using associated wrapper class'
     * {@code parseX} static method (except {@code char}, since
     * {@link Character} does not provide analogous functionality).
     * 
     * @param primitive
     *            Primitive type to create converter for
     * @return Converter instanc
     * @throws ReflectionException
     */
    private Converter forPrimitive(final Class<?> primitive)
            throws ReflectionException {
        Class<?> wrapper = Reflection.wrapperFor(primitive);
        String name = "parse" + capital(primitive.getName());
        Method method = Reflection.getMethod(wrapper, name, String.class);
        return new MethodWrapper(primitive, method);
    }

    /**
     * Capitalizes first letter of the string.
     */
    private static String capital(String string) {
        char first = string.charAt(0);
        char cap = Character.toUpperCase(first);
        return cap + string.substring(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(String literal, TypeToken<?> targetType)
            throws ConversionException {
        Converter converter = getConverter(targetType);
        return converter.convert(literal, targetType);
    }

    /**
     * Shorthand for {@code convert(literal, TypeToken.of(clazz)}. Unlike this
     * alternative, it checks if the returned object has the appropriate type.
     * 
     * @param literal
     *            String to convert
     * @param clazz
     *            Type to which the string is to be converted
     * @return value of the requested type
     * @throws ConversionException
     */
    public <T> T convert(String literal, Class<T> clazz)
            throws ConversionException {
        Object result = convert(literal, TypeToken.of(clazz));
        Class<T> wrapped = Reflection.wrap(clazz);
        return wrapped.cast(result);
    }

    /**
     * Adds an entry consisting of the specified converter and the rule saying
     * it can produce values of any subtype of the specified type.
     * 
     * @param type
     *            Type of values produced by the converter
     * @param converter
     *            Converter implementation
     */
    public void register(Class<?> type, Converter converter) {
        register(Types.subclassOf(type), converter);
    }

    /**
     * Adds an entry consisting of the specified rule and converter
     * implementation to the entry list.
     * 
     * @param criterion
     *            Family of supported types
     * @param converter
     *            Converter implenentation
     */
    public void register(Predicate<? super TypeToken<?>> criterion,
            Converter converter) {
        entries.add(new ConverterEntry(criterion, converter));
    }

    /**
     * Retrieves appropriate converter for the specified target type.
     * 
     * @param targetType
     *            Type for which to find the converter
     * @return Converter for the specified type
     * @throws ConversionException
     *             If there is no appropriate converter, or if there are
     *             multiple (ambiguity)
     */
    public Converter getConverter(TypeToken<?> targetType)
            throws ConversionException {
        ConverterEntry matchingEntry = null;
        List<ConverterEntry> ambiguity = null;

        for (ConverterEntry entry : entries) {
            if (entry.isSuitableFor(targetType)) {
                if (matchingEntry == null) {
                    matchingEntry = entry;
                } else {
                    if (ambiguity == null) {
                        ambiguity = Lists.newArrayList();
                        ambiguity.add(matchingEntry);
                    }
                    ambiguity.add(entry);
                }
            }
        }
        if (ambiguity == null) {
            if (matchingEntry != null) {
                return matchingEntry.getConverter();
            } else {
                throw new NoMatchingConverterException(targetType);
            }
        } else {
            throw new AmbiguousConversionException(targetType, ambiguity);
        }
    }

}
