package pl.edu.agh.ki.grieg.util.converters;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.reflect.TypeToken;

@RunWith(MockitoJUnitRunner.class)
public class ConverterMapTest extends ConversionTestBase{
    
    @Mock
    private Converter uriConverter;

    private URI testURI = URI.create("file:///somewhere");
    
    @Before
    public void setupConverter() throws ConversionException {
        when(uriConverter.convert("value", TypeToken.of(URI.class)))
                .thenReturn(testURI);
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

}
