package pl.edu.agh.ki.grieg.util.converters;

import com.google.common.reflect.TypeToken;

public interface Converter {

    Object convert(String literal, TypeToken<?> targetType)
            throws ConversionException;

}
