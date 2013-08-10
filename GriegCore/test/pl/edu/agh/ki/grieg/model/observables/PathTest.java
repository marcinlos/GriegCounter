package pl.edu.agh.ki.grieg.model.observables;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathTest {

    private static final String FIRST = "root.data";

    private static final String SECOND = "some.other.path";

    private Path first = new Path(FIRST);

    private Path second = new Path(SECOND);

    private Path empty = new Path("");

    @Test(expected = NullPointerException.class)
    public void throwsNpeWhenNullInConstructor() {
        new Path((String) null);
    }
    
    @Test
    public void varargsConstructorWorks() {
        Path path = new Path("some", "random.component");
        Path other = new Path("some.random.component");
        assertEquals(path, other);
        assertEquals(path.toString(), "some.random.component");
    }
    
    @Test(expected = InvalidPathFormatException.class)
    public void throwsInvalidPathFormatGivenInvalidPath() {
        new Path("this.is..not.a.valid.path");
    }

    @Test
    public void toStringReturnsPath() {
        assertEquals(first.toString(), FIRST);
        assertEquals(second.toString(), SECOND);
        assertEquals(empty.toString(), "");
    }

    @Test
    public void equalPathsAreEqual() {
        Path firstEq = new Path(FIRST);
        assertEquals(first, firstEq);
    }

    @Test
    public void nonequalPathsAreNotEqual() {
        assertNotEquals(first, second);
    }

    @Test
    public void hashCodesMatch() {
        Path firstEq = new Path(FIRST);
        assertEquals(first.hashCode(), firstEq.hashCode());
    }

    @Test
    public void emptyPathIsEmpty() {
        assertTrue(empty.isEmpty());
    }

    @Test
    public void nonemptyPathIsNotEmpty() {
        assertFalse(first.isEmpty());
    }

}
