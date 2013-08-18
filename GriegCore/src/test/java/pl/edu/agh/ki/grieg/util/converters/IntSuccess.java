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
public final class IntSuccess extends
        ConversionSuccessTestBase<Integer> {

    public IntSuccess(String string, int value) {
        super(string, value);
    }

    private static final Object[][] DATA = {
        {" 0", 0 },
        { "1 ", 1 },
        { " 1", 1},
        { " 1 ", 1}, 
        { "0", 0 },
        { "1", 1 },
        { "-1", -1 },
        { "66666666", 66666666 },
        { "-1234567", -1234567 },
        //{ "0x34", 0x34 }
    };

    @Parameters
    public static List<Object[]> params() {
        return Arrays.asList(DATA);
    }

    @Test
    public void canParsePrimitives() throws Exception {
        assertEquals(value, map.convert(string, TypeToken.of(int.class)));
    }
}