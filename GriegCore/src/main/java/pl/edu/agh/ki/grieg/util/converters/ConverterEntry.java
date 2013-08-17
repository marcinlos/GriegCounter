package pl.edu.agh.ki.grieg.util.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.reflect.TypeToken;

public final class ConverterEntry {

    private final Predicate<? super TypeToken<?>> typeMatcher;

    private final Converter converter;

    public ConverterEntry(Predicate<? super TypeToken<?>> typeMatcher,
            Converter converter) {
        this.typeMatcher = checkNotNull(typeMatcher);
        this.converter = checkNotNull(converter);
    }

    public boolean isSuitableFor(TypeToken<?> type) {
        return typeMatcher.apply(type);
    }

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
        return String.format("%s for %s",  converter, typeMatcher);
    }

}
