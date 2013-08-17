package pl.edu.agh.ki.grieg.util.converters;

import com.google.common.reflect.TypeToken;

public class NoMatchingConverterException extends ConversionException {

    private final TypeToken<?> targetType;

    public NoMatchingConverterException(TypeToken<?> targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return "No converter for type " + targetType + " found";
    }

}
