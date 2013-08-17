package pl.edu.agh.ki.grieg.util.converters;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.reflect.TypeToken;

@RunWith(Parameterized.class)
public final class FloatSuccess extends
        ConversionSuccessTestBase<Float> {

    private static final float DELTA = 1e-6f;

    public FloatSuccess(String string, float value) {
        super(string, value);
    }

    private static final Object[][] data = {
        {" 0", 0.0f },
        { "1 ", 1.0f },
        { " 1", 1.0f},
        { " 1 ", 1.0f},
        { "0", 0.0f },
        { "1", 1.0f },
        { "-1", -1.0f },
        { "4.15123", 4.15123f },
        { "-1.898989", -1.898989f},
        { "1.", 1.0f},
        { ".1", 0.1f},
        { " .3", 0.3f }
    };

    @Parameters
    public static List<Object[]> params() {
        return Arrays.asList(data);
    }
    
    private float actual() throws ConversionException {
        return (Float) map.convert(string, TypeToken.of(float.class));
    }

    @Test
    public void canParsePrimitives() throws Exception {
        assertEquals(value, actual(), DELTA);
    }
}