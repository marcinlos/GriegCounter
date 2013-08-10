package pl.edu.agh.ki.grieg.model.observables;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.not;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * Immutable class representing path in the model tree.
 * 
 * @author los
 */
public class Path {

    /** Path separator string */
    private static final String SEP = ".";

    /**
     * Pattern used to verify whether the string is a valid path. The leading
     * '|' allows the empty string.
     */
    private static final Pattern VERIFIER = Pattern.compile("|\\w+(\\.\\w+)*");

    /** Helper used to join path components */
    private static final Joiner JOINER = Joiner.on(SEP);

    /** Helper used to split path components */
    private static final Splitter SPLITTER = Splitter.on(SEP)
            .omitEmptyStrings();

    /**
     * Helper used to filter component paths, removing empty components in the
     * edge cases.
     */
    private static final Predicate<String> IS_EMPTY = new Predicate<String>() {
        @Override
        public boolean apply(String input) {
            return input.isEmpty();
        }
    };

    private static final Path EMPTY = new Path();

    private final String path;

    private final List<String> components;

    public Path(String... components) {
        this(join(components));
    }

    public Path(Iterable<String> components) {
        this(join(components));
    }

    public Path(String path) {
        this.path = checkNotNull(path);
        if (!verify(path)) {
            throw new InvalidPathFormatException(path);
        }
        this.components = ImmutableList.copyOf(SPLITTER.split(path));
    }

    public Path append(String name) {
        return new Path(path, checkNotNull(name));
    }

    public Path prepend(String name) {
        return new Path(name, checkNotNull(path));
    }

    private static String join(String... components) {
        return join(Arrays.asList(components));
    }

    private static String join(Iterable<String> components) {
        return JOINER.join(Iterables.filter(components, not(IS_EMPTY)));
    }

    public String head() {
        return components.isEmpty() ? "" : components.get(0);
    }

    public Path tail() {
        return components.isEmpty() ? EMPTY
                : new Path(Iterables.skip(components, 1));
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    public static boolean verify(String path) {
        return VERIFIER.matcher(path).matches();
    }

    public List<String> getComponents() {
        return components;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Path) {
            Path other = (Path) o;
            return path.equals(other.path);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

}