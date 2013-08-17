package pl.edu.agh.ki.grieg.util.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.reflect.TypeToken;

/**
 * Utility class, simplifying creation of type matchers used to choose an
 * appropriate converter.
 * 
 * @author los
 */
public final class Types {

    private Types() {
        // non-instantiable
    }

    /**
     * Creates a predicate satisfied only by types equal to the specified type,
     * e.g.
     * 
     * <pre>
     * Types.sameAs(type).apply(someType)
     * iff
     * type.equals(someType)
     * </pre>
     * 
     * @param type
     *            Type to check equality with
     * @return Predicate implementing functionality described above
     */
    public static Predicate<TypeToken<?>> sameAs(TypeToken<?> type) {
        return new IsSameAs(type);
    }

    /**
     * Creates a predicate satisfied only by types equal to the specified class,
     * e.g.
     * 
     * <pre>
     * Types.sameAs(clazz).apply(someType)
     * iff
     * TypeToken.of(clazz).equals(someType)
     * </pre>
     * 
     * @param type
     *            Type to check equality with
     * @return Predicate implementing functionality described above
     */
    public static Predicate<TypeToken<?>> sameAs(Class<?> type) {
        return sameAs(TypeToken.of(type));
    }

    /**
     * Creates a predicate satisfied by subtypes/implementations of the
     * specified class or interface.
     * 
     * @param type
     *            Base type
     * @return Predicate matching subtypes of the specified type
     */
    public static Predicate<TypeToken<?>> subclassOf(Class<?> type) {
        return new IsSubclassOf(type);
    }

    /**
     * Implementation of the equality-checking predicate. Could use plain
     * {@link Predicates#equalTo(Object)} at the cost of generic
     * {@link #toString()} implementation.
     */
    private static final class IsSameAs implements Predicate<TypeToken<?>> {
        private final TypeToken<?> type;

        public IsSameAs(TypeToken<?> type) {
            this.type = checkNotNull(type);
        }

        @Override
        public boolean apply(TypeToken<?> input) {
            return type.equals(input);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof IsSameAs) {
                IsSameAs other = (IsSameAs) o;
                return type.equals(other.type);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(type);
        }

        @Override
        public String toString() {
            return "IsSameAs[" + type + "]";
        }
    }

    /**
     * Implementation of the subtype-checking predicate.
     */
    private static final class IsSubclassOf implements Predicate<TypeToken<?>> {
        private final Class<?> type;

        public IsSubclassOf(Class<?> type) {
            this.type = checkNotNull(type);
        }

        @Override
        public boolean apply(TypeToken<?> input) {
            return type.isAssignableFrom(input.getRawType());
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof IsSubclassOf) {
                IsSubclassOf other = (IsSubclassOf) o;
                return type.equals(other.type);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(type);
        }

        @Override
        public String toString() {
            return "IsSubclassOf[" + type.getName() + "]";
        }
    }

}
