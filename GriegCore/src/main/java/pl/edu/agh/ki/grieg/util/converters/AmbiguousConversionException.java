package pl.edu.agh.ki.grieg.util.converters;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;

public class AmbiguousConversionException extends ConversionException {

    private static final String FORMAT =
            "Conversion to %s is ambiguous, following converters can "
                    + "perform it:\n";

    private final TypeToken<?> targetType;

    private final List<ConverterEntry> matchingConverters;

    public AmbiguousConversionException(TypeToken<?> targetType,
            List<ConverterEntry> matchingConverters) {
        this.targetType = targetType;
        this.matchingConverters = ImmutableList.copyOf(matchingConverters);
    }

    public List<ConverterEntry> getMatchingConverters() {
        return matchingConverters;
    }

    @Override
    public String toString() {
        String header = String.format(FORMAT, targetType);
        StringBuilder sb = new StringBuilder(header);
        int ord = 1;
        for (ConverterEntry entry : matchingConverters) {
            sb.append("  ").append(ord++).append(") ")
                    .append(entry).append('\n');
        }
        return sb.toString();
    }

}
