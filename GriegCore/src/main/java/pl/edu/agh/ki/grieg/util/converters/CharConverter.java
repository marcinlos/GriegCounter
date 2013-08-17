package pl.edu.agh.ki.grieg.util.converters;

import com.google.common.reflect.TypeToken;

/**
 * {@link Converter} implementation capable of producing {@code char} values. It
 * only supports truly one-character strings, possibly surrounded by whitespace
 * characters. No escape sequences are supported.
 * 
 * <p>
 * Note: this combination of features and restrictions makes it currently
 * impossible to parse whitespace char value.
 * 
 * @author los
 */
final class CharConverter implements Converter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(String literal, TypeToken<?> targetType)
            throws ConversionException {
        String trimmed = literal.trim();
        if (trimmed.length() == 1) {
            return trimmed.charAt(0);
        } else if (trimmed.length() > 1) {
            throw new ConversionException("String \"" + trimmed +
                    "\" is too long to be considered a character");
        } else {
            throw new ConversionException("Empty string cannot be " +
                    "parsed as a character");
        }
    }
}