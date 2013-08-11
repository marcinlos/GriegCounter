package pl.edu.agh.ki.grieg.model;

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
 * <p>
 * Path consists of sequence of zero or more alphanumeric identifiers, separated
 * by dots. That is, path is a string matching the following regex:
 * 
 * <pre>
 * epsilon|\w+(\.\w+)*
 * </pre>
 * 
 * 
 * @author los
 */
public class Path {

    /** Path separator string */
    private static final String SEP = ".";

    /** Pattern used to verify single components */
    private static final Pattern COMPONENT_VERIFIER = Pattern.compile("\\w+");

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

    /** Empty path */
    public static final Path EMPTY = new Path();

    /** String containing the full, joined representation of the path */
    private final String path;

    /** List of path components */
    private final List<String> components;

    /**
     * Creates the path concatenating the components. Each argument may be empty
     * (in which case it shall be ignored), or consist of one or more
     * components.
     * 
     * @param components
     *            Parts of the path
     * @throws InvalidPathFormatException
     *             If the concatenated parts do not form the valid path
     * @see #Path(Iterable)
     */
    public Path(String... components) {
        this(join(components));
    }

    /**
     * Creates the path concatenating the components. Each argument may be empty
     * (in which case it shall be ignored), or consist of one or more
     * components.
     * 
     * @param components
     *            Parts of the path
     * @throws InvalidPathFormatException
     *             If the concatenated parts do not form the valid path
     * @see #Path(String...)
     */
    public Path(Iterable<String> components) {
        this(join(components));
    }

    /**
     * Does the actual job of verifying the path and building it from the
     * components.
     * 
     * @param path
     *            String containing the full path
     */
    private Path(String path) {
        this.path = checkNotNull(path);
        if (!isValid(path)) {
            throw new InvalidPathFormatException(path);
        }
        this.components = ImmutableList.copyOf(SPLITTER.split(path));
    }

    /**
     * Creates new path, consisting of this one with {@code part} appended to
     * it. If the {@code part} is empty, the same path shall be returned. The
     * resulting string must form a valid path, lest
     * {@link InvalidPathFormatException} was thrown.
     * 
     * @param part
     *            Part of the path to be appended to this path
     * @return New path
     */
    public Path append(String part) {
        return part.isEmpty() ? this : new Path(path, part);
    }

    /**
     * Creates new path, consisting of this one with {@code part} prepended to
     * it. If the {@code part} is empty, the same path shall be returned. The
     * resulting string must form a valid path, lest
     * {@link InvalidPathFormatException} was thrown.
     * 
     * @param part
     *            Part of the path to be appended to this path
     * @return New path
     */
    public Path prepend(String part) {
        return new Path(part, checkNotNull(path));
    }

    /**
     * Joins the components, using dot as a separator.
     */
    private static String join(String... components) {
        return join(Arrays.asList(components));
    }

    /**
     * Joins the components, using dot as a separator.
     */
    private static String join(Iterable<String> components) {
        return JOINER.join(Iterables.filter(components, not(IS_EMPTY)));
    }

    /**
     * @return First component of the path, or empty string, if the path is
     *         empty.
     */
    public String head() {
        return components.isEmpty() ? "" : components.get(0);
    }

    /**
     * @return Path consisting of all the components of this one except the
     *         first, or empty path ({@link #EMPTY}) if this path is empty.
     */
    public Path tail() {
        return components.isEmpty() ? EMPTY
                : new Path(Iterables.skip(components, 1));
    }

    /**
     * @return {@code true} if this path is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return path.isEmpty();
    }

    /**
     * Checks whether the specified string is a valid path, i.e. if its format
     * is as specified in the {@link Path} javadoc.
     * 
     * @param path
     *            String to verify
     * @return {@code true} if the string represents valid path, {@code false}
     *         otherwise
     */
    public static boolean isValid(String path) {
        return path == null ? false : VERIFIER.matcher(path).matches();
    }

    /**
     * Checks whether the specified string is a valid component, i.e. if it is a
     * nonempty alphanumeric string.
     * 
     * @param component
     *            String to verify
     * @return {@code true} if the string represents valid component,
     *         {@code false} otherwise
     */
    public static boolean isValidComponent(String component) {
        return component == null ? false
                : COMPONENT_VERIFIER.matcher(component).matches();
    }

    /**
     * @return List of components of this path
     */
    public List<String> getComponents() {
        return components;
    }

    /**
     * Returns string representing this path, i.e. all its components separated
     * by the dot.
     */
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