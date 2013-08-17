package pl.edu.agh.ki.grieg.util.converters;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import com.google.common.reflect.TypeToken;

public class TypesTest {

    @Test
    public void exactlyByTypeMatchesTheSameType() {
        TypeToken<?> type = TypeToken.of(String.class);
        assertTrue(Types.same(type).apply(type));
    }
    
    @Test
    public void exactlyByClassMatchesTheSameType() {
        TypeToken<String> type = TypeToken.of(String.class);
        assertTrue(Types.same(String.class).apply(type));
    }
    
    @Test
    public void isSubclassWorks() {
        Class<?> parent = List.class;
        Class<?> child = ArrayList.class;
        assertTrue(Types.subclassOf(parent).apply(TypeToken.of(child)));
    }

}
