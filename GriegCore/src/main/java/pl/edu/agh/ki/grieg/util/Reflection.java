package pl.edu.agh.ki.grieg.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public final class Reflection {

    private Reflection() {
        // non-instantiable
    }

    private static final BiMap<Class<?>, Class<?>> wrappers;

    private static final Map<Class<?>, Class<?>> wrappees;

    static {
        wrappers = ImmutableBiMap.<Class<?>, Class<?>> builder()
                .put(int.class, Integer.class)
                .put(byte.class, Byte.class)
                .put(char.class, Character.class)
                .put(short.class, Short.class)
                .put(long.class, Long.class)
                .put(float.class, Float.class)
                .put(double.class, Double.class)
                .build();
        wrappees = wrappers.inverse();
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

    public static Method getMethod(Class<?> from, String method,
            Class<?>... args) throws ReflectionException {
        try {
            return from.getMethod(method, args);
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeStatic(Method method, Object... args)
            throws ReflectionException {
        try {
            return (T) method.invoke(null, args);
        } catch (IllegalAccessException e) {
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

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> castClass(Class<?> clazz) {
        return (Class<? extends T>) clazz;
    }

    public static Class<?> wrapperFor(Class<?> primitive) {
        return wrappers.get(primitive);
    }

    public static Class<?> wrappedBy(Class<?> wrapper) {
        return wrappees.get(wrapper);
    }

}
