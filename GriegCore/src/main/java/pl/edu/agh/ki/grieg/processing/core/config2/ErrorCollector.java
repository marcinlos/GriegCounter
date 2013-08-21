package pl.edu.agh.ki.grieg.processing.core.config2;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Gathers arbitrary amount of exceptions and stores them in a list.
 * 
 * @author los
 */
public class ErrorCollector implements ErrorHandler {

    /** List of exceptions */
    private final List<Throwable> exceptions = Lists.newArrayList();

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Throwable e) {
        exceptions.add(e);
    }

    /**
     * @return Immutable list of all the exceptions received by this
     *         {@link ErrorCollector}
     */
    public List<Throwable> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }

}
