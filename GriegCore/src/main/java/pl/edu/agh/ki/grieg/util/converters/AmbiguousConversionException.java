package pl.edu.agh.ki.grieg.util.converters;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;

/**
 * Exception thrown by the {@link ConverterMap} when there are multiple
 * converters declared as capable of converting to the requested type. It
 * contains all the information about ambiguity to ease debugging.
 * 
 * @author los
 */
public class AmbiguousConversionException extends ConversionException {

    private static final String FORMAT =
            "Conversion to %s is ambiguous, following converters can "
                    + "perform it:\n";

    /** Target type of the failed conversion */
    private final TypeToken<?> targetType;

    /** List of matching converters */
    private final List<ConverterEntry> matchingEntries;

    /**
     * Creates new {@link AmbiguousConversionException} with specified type and
     * caused by the specified converters.
     * 
     * @param targetType
     *            Target type of the conversion
     * @param matchingEntries
     *            Offending multiple converters
     */
    public AmbiguousConversionException(TypeToken<?> targetType,
            List<ConverterEntry> matchingEntries) {
        super(formatMessage(targetType, matchingEntries));
        this.targetType = targetType;
        this.matchingEntries = ImmutableList.copyOf(matchingEntries);
    }

    /**
     * @return List of matching converters
     */
    public List<ConverterEntry> getMatchingEntries() {
        return matchingEntries;
    }

    /**
     * @return Target type of the failed conversion
     */
    public TypeToken<?> getTargetType() {
        return targetType;
    }

    /**
     * Creates the textual representation of the failure, indicating target
     * type and enumerating matching converter entries. 
     */
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
