package pl.edu.agh.ki.grieg.util.converters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.reflect.TypeToken;

public class CharConverterTest {
    
    private static final TypeToken<?> CHAR_TYPE = TypeToken.of(char.class);

    private CharConverter converter;
    
    @Before
    public void setup() {
        converter = new CharConverter();
    }
    
    @Test
    public void canConvertSingleLetter() throws ConversionException {
        assertEquals('c', converter.convert("c", CHAR_TYPE));
    }
    
    @Test
    public void canConvertLetterWithWhitespace() throws ConversionException {
        assertEquals('c', converter.convert(" \t\n\n\tc  \n", CHAR_TYPE));
    }
    
    @Test(expected = ConversionException.class)
    public void cannotConvertEmptyString() throws ConversionException {
        converter.convert("", CHAR_TYPE);
    }
    
    @Test(expected = ConversionException.class)
    public void cannotConvertPureWhitespaceString() throws ConversionException {
        converter.convert("   \n\t\r \t\t    \n", CHAR_TYPE);
    }

    @Test(expected = ConversionException.class)
    public void cannotConvertMultipleCharacters() throws ConversionException {
        converter.convert("ab", CHAR_TYPE);
    }

}
