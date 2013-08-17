package pl.edu.agh.ki.grieg.util.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.reflect.TypeToken;

public final class Types {

    private Types() {
        // non-instantiable
    }
    
    public static Predicate<TypeToken<?>> sameAs(TypeToken<?> type) {
        return new IsSameAs(type);
    }
    
    public static Predicate<TypeToken<?>> sameAs(Class<?> type) {
        return sameAs(TypeToken.of(type));
    }
    
    public static Predicate<TypeToken<?>> subclassOf(Class<?> type) {
        return new IsSubclassOf(type);
    }
    
    private static final class IsSameAs implements Predicate<TypeToken<?>> {
        private final TypeToken<?> type;
        
        public IsSameAs(TypeToken<?> type) {
            this.type = checkNotNull(type);
        }

        @Override
        public boolean apply(TypeToken<?> input) {
            return type.isAssignableFrom(input.getRawType());
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
