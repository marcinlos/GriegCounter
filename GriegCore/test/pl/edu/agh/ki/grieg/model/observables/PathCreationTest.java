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
public class PathCreationTest {
    
    private static final Object[][] params = {
        { Arrays.asList("some", "random", "path"), "some.random.path",  },
        { Arrays.asList("", "some", "path"), "some.path" },
        { Arrays.asList("some", "path", "", ""), "some.path" },
        { Arrays.asList("some", "", "path", ""), "some.path" },
        { Arrays.asList("", "", ""), "" },
        { Arrays.asList("some.random", "path"), "some.random.path" },
        { Arrays.asList("some.random", "", "", "path"), "some.random.path" },
        { Arrays.asList("some.random", "path.blah"), "some.random.path.blah" },
        { Arrays.asList("", "", "some", "", "random.path", "", "", "", 
                "blah", ""), "some.random.path.blah" }
    };
    
    private Path path;
    private String string;
    
    public PathCreationTest(List<String> components, String path) {
        this.path = new Path(components);
        this.string = path;
    }
    
    @Test
    public void componentsAreCorrect() {
        assertEquals("For '" + string + "'", string, path.toString());
    }
    
    
    @Parameters
    public static Collection<Object[]> parmas() {
        return Arrays.asList(params);
    }
    
}