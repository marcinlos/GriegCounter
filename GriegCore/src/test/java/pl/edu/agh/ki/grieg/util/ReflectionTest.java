package pl.edu.agh.ki.grieg.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

abstract class Abstract {
    
    public Abstract() {
        // empty
    }
    
}

class Private { 
    private Private() {
        // empty
    }
}

class Throwing {
    public Throwing() {
        throw new AssertionError("Don't instantiate me, please");
    }
}

public class ReflectionTest {

    @Test
    public void wrapsPrimitiveTypes() {
        assertEquals(Integer.class, Reflection.wrap(int.class));
        assertEquals(Character.class, Reflection.wrap(char.class));
    }

    @Test
    public void wrapDoesNotChangeReferenceTypes() {
        assertEquals(byte[].class, Reflection.wrap(byte[].class));
        assertEquals(String.class, Reflection.wrap(String.class));
    }

    @Test
    public void canGetClassAsSubclass() throws ReflectionException {
        Class<?> clazz = Reflection.getClass("java.util.ArrayList", List.class);
        assertEquals(ArrayList.class, clazz);
    }

    @Test(expected = ClassCastException.class)
    public void cannotInterpretAsSubclass() throws ReflectionException {
        Reflection.getClass("java.lang.String", Integer.class);
    }

    @Test(expected = ReflectionException.class)
    public void cannotInstantiateAbstract() throws ReflectionException {
        Reflection.create(Abstract.class);
    }

    @Test(expected = ReflectionException.class)
    public void cannotInstantiateWithInvalidParameters()
            throws ReflectionException {
        Reflection.create(Integer.class, "blabla", 3, 15.4);
    }
    
    @Test(expected = ReflectionException.class)
    public void cannotInstantiateWithPrivateConstructor()
            throws ReflectionException {
        Reflection.create(Private.class);
    }
    
    @Test(expected = ReflectionException.class)
    public void forwardsExceptionInConstructor() throws ReflectionException {
        Reflection.create(Throwing.class);
    }
    
    @Test
    public void canCreateSome() throws ReflectionException {
        assertEquals("str", Reflection.create(String.class, "str"));
    }
    
    @Test
    public void canCreateSomeByName() throws ReflectionException {
        assertEquals("str", Reflection.create("java.lang.String", "str"));
    }


}
