package pl.edu.agh.ki.grieg.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator implementation created to provide {@code Iterable} interface for any
 * random access sequence which does not provide it by itself (especially plain
 * Java arrays). Stores index and max length, uses abstract method to fetch an
 * element, since primitive type arrays cannot be represented using generic
 * parameter. Array type itself can be a generic parameter, however it wouldn't
 * be possible to fetch array elements, since arrays have no interface that
 * could be used as a generic parameter's bound.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of sequence elements
 */
abstract class AbstractArrayIterator<T> implements Iterator<T> {

    /** Length of the sequence */
    private int length;

    /** Index of the next element to be yielded */
    private int i = 0;

    /**
     * Creates new iterator for sequence of {@code length} elements.
     * 
     * @param length
     *            Length of the sequence
     */
    public AbstractArrayIterator(int length) {
        this.length = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return i < length;
    }

    /**
     * {@inheritDoc}
     * 
     * Updates internal counter.
     */
    @Override
    public T next() {
        if (hasNext()) {
            return getItem(i++);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Removal is unsupported by this {@code Iterator}. Invocation results in
     * {@code UnsupportedOperationException}.
     * 
     * @throws UnsupportedOperationException
     *             Always
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException(
                "AbstractArrayIterator cannot remove items");
    }

    /**
     * Provides random access to wrapped sequence.
     * 
     * @param i
     *            Element index
     * @return {@code i}th element of the sequence
     */
    protected abstract T getItem(int i);

}
