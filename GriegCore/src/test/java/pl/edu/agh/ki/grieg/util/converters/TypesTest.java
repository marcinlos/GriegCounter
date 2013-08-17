package pl.edu.agh.ki.grieg.util.converters;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.reflect.TypeToken;

public class TypesTest {
    
    Predicate<TypeToken<?>> intMatcher;
    Predicate<TypeToken<?>> otherIntMatcher;
    Predicate<TypeToken<?>> subIntMatcher;

    Predicate<TypeToken<?>> listMatcher;
    Predicate<TypeToken<?>> subListMatcher;
    Predicate<TypeToken<?>> arrayListMatcher;
    
    @Before
    public void setupMatchers() {
        intMatcher = Types.sameAs(int.class);
        otherIntMatcher = Types.sameAs(int.class);
        subIntMatcher = Types.subclassOf(int.class);
        
        listMatcher = Types.sameAs(List.class);
        subListMatcher = Types.subclassOf(List.class);
        arrayListMatcher = Types.subclassOf(ArrayList.class);
    }

    @Test
    public void exactlyByTypeMatchesTheSameType() {
        TypeToken<?> type = TypeToken.of(String.class);
        assertTrue(Types.sameAs(type).apply(type));
    }
    
    @Test
    public void exactlyByClassMatchesTheSameType() {
        TypeToken<?> type = TypeToken.of(String.class);
        assertTrue(Types.sameAs(String.class).apply(type));
    }
    
    @Test
    public void exactlyByClassDoesNotMatchSubtype() {
        TypeToken<?> subtype = TypeToken.of(ArrayList.class);
        assertFalse(Types.sameAs(List.class).apply(subtype));
    }
    
    @Test
    public void isSubclassWorks() {
        TypeToken<?> child = TypeToken.of(ArrayList.class);
        assertTrue(subListMatcher.apply(child));
    }
    
    @Test
    public void typeMatchersHaveValueSemantics() {
        assertEquals(intMatcher, otherIntMatcher);
        assertNotEquals(intMatcher, subIntMatcher);
        
        assertNotEquals(subListMatcher, listMatcher);
        assertNotEquals(listMatcher, arrayListMatcher);
    }

}
