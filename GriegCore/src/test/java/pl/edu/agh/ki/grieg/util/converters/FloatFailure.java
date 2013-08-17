package pl.edu.agh.ki.grieg.util.converters;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.reflect.TypeToken;

@RunWith(Parameterized.class)
public final class FloatFailure extends ConversionFailureTestBase {

    private final String string;

    public FloatFailure(String string) {
        this.string = string;
    }

    private static final String[] data = {
        "--1", "dfsdf", "1324fd", "df332", "1,0", "0,5", ",0", "1,", 
        "1.ds", "1..834"
    };

    @Parameters
    public static List<Object[]> params() {
        return prepare(data);
    }
    

    @Test(expected = ConversionException.class)
    public void canParsePrimitives() throws Exception {
        map.convert(string, TypeToken.of(float.class));
    }
}