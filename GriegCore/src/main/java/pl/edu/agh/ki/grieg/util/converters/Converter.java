package pl.edu.agh.ki.grieg.util.converters;

import com.google.common.reflect.TypeToken;

/**
 * Interface of a general conversion functionality provider. For requested type,
 * converter is supposed to parse specified string and produce value of the
 * requested type, or fail and throw a {@link ConversionException}.
 * 
 * @author los
 */
public interface Converter {

    /**
     * Converts specified {@code literal} to an instance of the specified type.
     * 
     * @param literal
     *            String to convert
     * @param targetType
     *            Type to which the string is to be converted
     * @return Value of the requested type
     * @throws ConversionException
     *             If the conversion proves impossible
     */
    Object convert(String literal, TypeToken<?> targetType)
            throws ConversionException;

}
