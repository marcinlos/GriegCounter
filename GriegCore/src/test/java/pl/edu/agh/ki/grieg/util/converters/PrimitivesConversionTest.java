package pl.edu.agh.ki.grieg.util.converters;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.reflect.TypeToken;

@RunWith(Enclosed.class)
public class PrimitivesConversionTest {

    @RunWith(Parameterized.class)
    public static final class IntSuccess extends
            ConversionSuccessTestBase<Integer> {

        public IntSuccess(String string, int value) {
            super(string, value);
        }

        private static final Object[][] data = {
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
            return Arrays.asList(data);
        }

        @Test
        public void canParsePrimitives() throws Exception {
            assertEquals(value, map.convert(string, TypeToken.of(int.class)));
        }
    }

    @RunWith(Parameterized.class)
    public static final class IntFailure extends ConversionFailureTestBase {

        private final String string;

        public IntFailure(String string) {
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

    @RunWith(Parameterized.class)
    public static final class FloatSuccess extends
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

    @RunWith(Parameterized.class)
    public static final class FloatFailure extends ConversionFailureTestBase {

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

}
