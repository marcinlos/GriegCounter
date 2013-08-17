package pl.edu.agh.ki.grieg.util.converters;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;

public class AmbiguousConversionException extends ConversionException {

    private static final String FORMAT =
            "Conversion to %s is ambiguous, following converters can "
                    + "perform it:\n";

    private final TypeToken<?> targetType;

    private final List<ConverterEntry> matchingEntries;

    public AmbiguousConversionException(TypeToken<?> targetType,
            List<ConverterEntry> matchingEntries) {
        super(formatMessage(targetType, matchingEntries));
        this.targetType = targetType;
        this.matchingEntries = ImmutableList.copyOf(matchingEntries);
    }

    public List<ConverterEntry> getMatchingEntries() {
        return matchingEntries;
    }
    
    public TypeToken<?> getTargetType() {
        return targetType;
    }

    private static String formatMessage(TypeToken<?> targetType,
            List<ConverterEntry> matchingEntries) {
        String header = String.format(FORMAT, targetType);
        StringBuilder sb = new StringBuilder(header);
        int ord = 1;
        for (ConverterEntry entry : matchingEntries) {
            sb.append("  ").append(ord++).append(") ")
                    .append(entry).append('\n');
        }
        return sb.toString();
    }

}
