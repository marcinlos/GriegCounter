package pl.edu.agh.ki.grieg.util.converters;

import com.google.common.reflect.TypeToken;

/**
 * Exception thrown to indicate that no {@link Converter} capable of producing
 * values of the requested type was found, and so the conversion could not be
 * successfully performed.
 * 
 * @author los
 */
public class NoMatchingConverterException extends ConversionException {

    /** Target type of the failed conversion */
    private final TypeToken<?> targetType;

    /**
     * Creates new {@link NoMatchingConverterException} with specified target
     * conversion type.
     * 
     * @param targetType
     *            Target type of the failed conversion
     */
    public NoMatchingConverterException(TypeToken<?> targetType) {
        super(formatMessage(targetType));
        this.targetType = targetType;
    }

    /**
     * @return Target type of the failed conversion
     */
    public TypeToken<?> getTargetType() {
        return targetType;
    }

    /**
     * Creates a textual representation of the error
     */
    private static String formatMessage(TypeToken<?> targetType) {
        return "No converter for type " + targetType + " found";
    }

}
