package pl.edu.agh.ki.grieg.model.observables;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import pl.edu.agh.ki.grieg.model.model.Path;

@RunWith(Parameterized.class)
public class PathComponentsTest {
    
    private static final Object[][] params = {
        { "some.random.path", Arrays.asList("some", "random", "path") },
        { "", Arrays.asList() },
        { "path", Arrays.asList("path") }
    };
    
    private String path;
    private List<String> components;
    
    public PathComponentsTest(String path, List<String> components) {
        this.path = path;
        this.components = components;
    }
    
    @Test
    public void componentsAreCorrect() {
        List<String> computed = new Path(path).getComponents();
        assertEquals("For '" + path + "'", components, computed);
    }
    
    
    @Parameters
    public static Collection<Object[]> parmas() {
        return Arrays.asList(params);
    }
    
}