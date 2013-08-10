package pl.edu.agh.ki.grieg.model.observables;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PathVerificationTest {

    private static final Object[][] params = {
        { "some.random.path", true },
        { "other", true },
        { "", true },
        { "wE_iRD.8Ut.c4n_____8989.be.3", true },
        { "invalid.", false },
        { ".", false },
        { ".wrong", false },
        { "..", false },
        { "incorrect..path", false }
    };

    private String path;
    private boolean valid;

    public PathVerificationTest(String path, boolean valid) {
        this.path = path;
        this.valid = valid;
    }

    @Test
    public void matchesExpectations() {
        boolean isOk = Path.isValid(path);
        assertEquals("For '" + path + "'", valid, isOk);
    }
    
    @Test
    public void nullIsNotValid() {
        assertFalse(Path.isValid(null));
    }

    @Parameters
    public static Collection<Object[]> parmas() {
        return Arrays.asList(params);
    }

}