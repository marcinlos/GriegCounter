package pl.edu.agh.ki.grieg.processing.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class Reflection {

    private Reflection() {
        // non-instantiable
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getClass(String name)
            throws ReflectionException {
        try {
            return (Class<? extends T>) Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> Class<? extends T> getClass(String name, Class<T> clazz)
            throws ReflectionException {
        Class<? extends T> fromName = getClass(name);
        return fromName.asSubclass(clazz);
    }

    public static <T> T create(String name, Object... args)
            throws ReflectionException {
        Class<? extends T> clazz = getClass(name);
        return create(clazz, args);
    }

    public static <T> T create(Class<T> clazz, Object... args)
            throws ReflectionException {
        try {
            Constructor<T> ctor = clazz.getConstructor(toClassArray(args));
            return ctor.newInstance(args);
        } catch (InstantiationException e) {
            throw new ReflectionException(e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException(e);
        } catch (InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    private static Class<?>[] toClassArray(Object[] args) {
        Class<?>[] classes = new Class<?>[args.length];
        int i = 0;
        for (Object arg : args) {
            classes[i++] = arg.getClass();
        }
        return classes;
    }

}
