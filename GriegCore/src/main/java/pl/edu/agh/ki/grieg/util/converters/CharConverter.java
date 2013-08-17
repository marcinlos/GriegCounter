package pl.edu.agh.ki.grieg.util.converters;

import com.google.common.reflect.TypeToken;

final class CharConverter implements Converter {
    
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