package pl.edu.agh.ki.grieg.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import pl.edu.agh.ki.grieg.model.Path;

@RunWith(Parameterized.class)
public class PathCombinatorsTest {
    
    private static final Object[][] params = {
        { "some.4.random.pa_th", "some", "4.random.pa_th" },
        { "", "", "" },
        { "path", "path", "" }
    };
    
    private Path path;
    private String head;
    private String tail;
    
    public PathCombinatorsTest(String path, String head, String tail) {
        this.path = new Path(path);
        this.head = head;
        this.tail = tail;
    }
    
    @Test
    public void headsAreCorrect() {
        String computed = path.head();
        assertEquals("For '" + path + "'", head, computed);
    }
    
    @Test
    public void tailsAreCorrect() {
        Path computed = path.tail();
        assertEquals("For '" + path + "'", computed, new Path(tail));
    }
    
    @Test
    public void prependWorks() {
        Path p = new Path(tail);
        assertEquals(path, p.prepend(head));
    }
    
    @Test
    public void appendWorks() {
        Path p = new Path(head);
        assertEquals(path, p.append(tail));
    }
    
    
    @Parameters
    public static Collection<Object[]> parmas() {
        return Arrays.asList(params);
    }
    
}