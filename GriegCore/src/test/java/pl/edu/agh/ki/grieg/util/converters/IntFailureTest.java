package pl.edu.agh.ki.grieg.util.converters;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.reflect.TypeToken;

@RunWith(Parameterized.class)
public final class IntFailureTest extends ConversionFailureTestBase {

    private final String string;

    public IntFailureTest(String string) {
        this.string = string;
    }

    private static final String[] data = {
        "--1", "dfsdf", "1324fd", "df332", "abd", "0x343", "#34"
    };

    @Parameters
    public static List<Object[]> params() {
        return prepare(data);
    }

    @Test(expected = ConversionException.class)
    public void canParsePrimitives() throws Exception {
        map.convert(string, TypeToken.of(int.class));
        
    }
}