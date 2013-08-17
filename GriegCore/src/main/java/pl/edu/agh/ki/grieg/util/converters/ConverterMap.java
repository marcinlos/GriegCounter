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

public class ConverterMap implements Converter {

    private static final Logger logger = LoggerFactory
            .getLogger(ConverterMap.class);
    
    private static final class MethodWrapper implements Converter {

        private final Class<?> primitive;
        private final Method method;

        MethodWrapper(Class<?> primitive, Method method) {
            this.primitive = primitive;
            this.method = method;
        }

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

    private final Set<ConverterEntry> entries = Sets.newLinkedHashSet();

    public ConverterMap() {
        registerBuiltins();
    }

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

    private Converter forPrimitive(final Class<?> primitive)
            throws ReflectionException {
        Class<?> wrapper = Reflection.wrapperFor(primitive);
        String name = "parse" + capital(primitive.getName());
        Method method = Reflection.getMethod(wrapper, name, String.class);
        return new MethodWrapper(primitive, method);
    }

    private static String capital(String string) {
        char first = string.charAt(0);
        char cap = Character.toUpperCase(first);
        return cap + string.substring(1);
    }

    @Override
    public Object convert(String literal, TypeToken<?> targetType)
            throws ConversionException {
        Converter converter = getConverter(targetType);
        return converter.convert(literal, targetType);
    }

    public <T> T convert(String literal, Class<T> clazz)
            throws ConversionException {
        Object result = convert(literal, TypeToken.of(clazz));
        return clazz.cast(result);
    }

    public void register(Class<?> type, Converter converter) {
        register(Types.subclassOf(type), converter);
    }

    public void register(Predicate<? super TypeToken<?>> criterion,
            Converter converter) {
        entries.add(new ConverterEntry(criterion, converter));
    }

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
