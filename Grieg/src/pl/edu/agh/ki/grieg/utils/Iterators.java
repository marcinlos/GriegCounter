package pl.edu.agh.ki.grieg.utils;

import java.util.Iterator;

public class Iterators {

    private Iterators() {
        // non-instantiable
    }

    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code byte} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(byte[])
     */
    public static Iterable<Byte> iterable(final byte[] array) {
        return new Iterable<Byte>() {
            @Override
            public Iterator<Byte> iterator() {
                return Iterators.iterator(array);
            }
        };
    }
    
    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code char} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(char[])
     */
    public static Iterable<Character> iterable(final char[] array) {
        return new Iterable<Character>() {
            @Override
            public Iterator<Character> iterator() {
                return Iterators.iterator(array);
            }
        };
    }
    
    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code short} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(short[])
     */
    public static Iterable<Short> iterable(final short[] array) {
        return new Iterable<Short>() {
            @Override
            public Iterator<Short> iterator() {
                return Iterators.iterator(array);
            }
        };
    }
    
    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code int} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(int[])
     */
    public static Iterable<Integer> iterable(final int[] array) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return Iterators.iterator(array);
            }
        };
    }
    
    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code long} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(long[])
     */
    public static Iterable<Long> iterable(final long[] array) {
        return new Iterable<Long>() {
            @Override
            public Iterator<Long> iterator() {
                return Iterators.iterator(array);
            }
        };
    }
    
    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code float} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(float[])
     */
    public static Iterable<Float> iterable(final float[] array) {
        return new Iterable<Float>() {
            @Override
            public Iterator<Float> iterator() {
                return Iterators.iterator(array);
            }
        };
    }
    
    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code double} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(double[])
     */
    public static Iterable<Double> iterable(final double[] array) {
        return new Iterable<Double>() {
            @Override
            public Iterator<Double> iterator() {
                return Iterators.iterator(array);
            }
        };
    }
    
    /**
     * Creates an {@linkplain Iterable} wrapper for a {@code T} array.
     * 
     * @param array
     *            Array to wrap
     * @return {@code Iterable} instance
     * @see #iterator(T[])
     */
    public static <T> Iterable<T> iterable(final T[] array) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.iterator(array);
            }
        };
    }

    /**
     * Creates an iterator traversing {@code byte} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static Iterator<Byte> iterator(final byte[] array) {
        return new AbstractArrayIterator<Byte>(array.length) {
            @Override
            protected Byte getItem(int i) {
                return array[i];
            }
        };
    }
    
    /**
     * Creates an iterator traversing {@code char} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static Iterator<Character> iterator(final char[] array) {
        return new AbstractArrayIterator<Character>(array.length) {
            @Override
            protected Character getItem(int i) {
                return array[i];
            }
        };
    }
    
    /**
     * Creates an iterator traversing {@code short} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static Iterator<Short> iterator(final short[] array) {
        return new AbstractArrayIterator<Short>(array.length) {
            @Override
            protected Short getItem(int i) {
                return array[i];
            }
        };
    }
    
    /**
     * Creates an iterator traversing {@code int} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static Iterator<Integer> iterator(final int[] array) {
        return new AbstractArrayIterator<Integer>(array.length) {
            @Override
            protected Integer getItem(int i) {
                return array[i];
            }
        };
    }
    
    /**
     * Creates an iterator traversing {@code long} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static Iterator<Long> iterator(final long[] array) {
        return new AbstractArrayIterator<Long>(array.length) {
            @Override
            protected Long getItem(int i) {
                return array[i];
            }
        };
    }
    
    /**
     * Creates an iterator traversing {@code float} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static Iterator<Float> iterator(final float[] array) {
        return new AbstractArrayIterator<Float>(array.length) {
            @Override
            protected Float getItem(int i) {
                return array[i];
            }
        };
    }
    
    /**
     * Creates an iterator traversing {@code double} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static Iterator<Double> iterator(final double[] array) {
        return new AbstractArrayIterator<Double>(array.length) {
            @Override
            protected Double getItem(int i) {
                return array[i];
            }
        };
    }
    
    /**
     * Creates an iterator traversing {@code T} array in order of increasing
     * indices.
     * 
     * @param array
     *            Array to traverse
     * @return {@code Iterator} object
     */
    public static <T> Iterator<T> iterator(final T[] array) {
        return new AbstractArrayIterator<T>(array.length) {
            @Override
            protected T getItem(int i) {
                return array[i];
            }
        };
    }

}
