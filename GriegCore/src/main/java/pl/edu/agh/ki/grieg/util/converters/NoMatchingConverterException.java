package pl.edu.agh.ki.grieg.util.converters;

import com.google.common.reflect.TypeToken;

public class NoMatchingConverterException extends ConversionException {

    private final TypeToken<?> targetType;

    public NoMatchingConverterException(TypeToken<?> targetType) {
        super(formatMessage(targetType));
        this.targetType = targetType;
    }

    public TypeToken<?> getTargetType() {
        return targetType;
    }
    
    private static String formatMessage(TypeToken<?> targetType) {
        return "No converter for type " + targetType + " found";
    }

}
