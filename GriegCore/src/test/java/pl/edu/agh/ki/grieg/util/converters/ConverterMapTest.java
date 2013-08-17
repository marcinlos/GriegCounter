package pl.edu.agh.ki.grieg.util.converters;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Predicate;
import com.google.common.reflect.TypeToken;

@RunWith(MockitoJUnitRunner.class)
public class ConverterMapTest extends ConversionTestBase {

    @Mock private Converter uriConverter;
    @Mock private Converter otherConverter;
    @Mock private Converter yetAnotherConverter;

    private URI testURI = URI.create("file:///somewhere");

    @Before
    public void setupConverter() throws ConversionException {
        when(uriConverter.convert("value", TypeToken.of(URI.class)))
                .thenReturn(testURI);
        when(otherConverter.convert(anyString(), any(TypeToken.class)))
                .thenThrow(new ConversionException());
        when(yetAnotherConverter.convert(anyString(), any(TypeToken.class)))
                .thenThrow(new ConversionException());
    }
    
    @Test
    public void canParseStrings() throws Exception {
        String string = "   come random #$@#$# strange string \\\\\\\\";
        assertEquals(string, map.convert(string, String.class));
    }

    @Test
    public void canParseCustomTypes() throws Exception {
        map.register(URI.class, uriConverter);
        assertEquals(testURI, map.convert("value", URI.class));
    }

    @Test
    public void addIsIdempotent() throws Exception {
        map.register(URI.class, uriConverter);
        map.register(URI.class, uriConverter);
        assertEquals(testURI, map.convert("value", URI.class));
    }
    
    private void causeAmbiguity() throws ConversionException {
        map.register(URI.class, uriConverter);
        map.register(URI.class, otherConverter);
        map.register(Types.sameAs(URI.class), yetAnotherConverter);
        map.convert("value", URI.class);
    }

    @Test(expected = AmbiguousConversionException.class)
    public void ambiguityCausesFailure() throws Exception {
        causeAmbiguity();
    }

    @Test
    public void ambiguityExceptionIsInformative() throws Exception {
        try {
            causeAmbiguity();
        } catch (AmbiguousConversionException e) {
            checkException(e);
        }
    }

    private void checkException(AmbiguousConversionException e) {
        Predicate<TypeToken<?>> matcher = Types.subclassOf(URI.class);
        ConverterEntry[] entries = {
                new ConverterEntry(matcher, uriConverter),
                new ConverterEntry(matcher, otherConverter),
                new ConverterEntry(Types.sameAs(URI.class), yetAnotherConverter)
        };
        assertEquals(Arrays.asList(entries), e.getMatchingEntries());
        assertEquals(TypeToken.of(URI.class), e.getTargetType());
        
        String message = e.getMessage();
        assertThat(message, containsString(URI.class.getName()));
        assertThat(message, containsString(uriConverter.toString()));
        assertThat(message, containsString(otherConverter.toString()));
    }
    
    @Test(expected = NoMatchingConverterException.class)
    public void throwsWhenThereIsNoConverter() throws Exception {
        map.convert("bla", Runtime.class);
    }
    
    @Test
    public void noMatchingConverterExceptionIsInformative() throws Exception {
        try {
            map.convert("bla", Runtime.class);
        } catch (NoMatchingConverterException e) {
            assertEquals(TypeToken.of(Runtime.class), e.getTargetType());
            String message = e.getMessage();
            assertThat(message, containsString(Runtime.class.getName()));
        }
    }
    
}
