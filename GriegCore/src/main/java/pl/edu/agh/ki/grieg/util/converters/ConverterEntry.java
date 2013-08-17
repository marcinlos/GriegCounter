package pl.edu.agh.ki.grieg.util.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.reflect.TypeToken;

/**
 * Structure describing single converter binding, specified by the converter,
 * and the family of types that it can produce. Instances of this class are
 * immutable, and have appropriately defined {@link #equals(Object)} and
 * {@link #hashCode()}.
 * 
 * @author los
 */
public final class ConverterEntry {

    /** Restriction on the types the associated converter can produce */
    private final Predicate<? super TypeToken<?>> typeMatcher;

    /** Converter implementation */
    private final Converter converter;

    /**
     * Creates new entry for the specified {@code (condition, converter)} pair.
     * 
     * @param typeMatcher
     *            Predicate that determines supported types
     * @param converter
     *            Converter implementation
     */
    public ConverterEntry(Predicate<? super TypeToken<?>> typeMatcher,
            Converter converter) {
        this.typeMatcher = checkNotNull(typeMatcher);
        this.converter = checkNotNull(converter);
    }

    /**
     * Checks whether the converter can be used to produce specified type.
     * Delegates to contained predicate.
     * 
     * @param type
     *            Type to be checked
     * @return {@code true} if the type can be produced by the converter,
     *         {@code false} otherwise
     */
    public boolean isSuitableFor(TypeToken<?> type) {
        return typeMatcher.apply(type);
    }

    /**
     * @return Contained converter
     */
    public Converter getConverter() {
        return converter;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ConverterEntry) {
            ConverterEntry other = (ConverterEntry) o;
            return typeMatcher.equals(other.typeMatcher)
                    && converter.equals(other.converter);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(typeMatcher, converter);
    }

    @Override
    public String toString() {
        return String.format("%s for %s", converter, typeMatcher);
    }

}
