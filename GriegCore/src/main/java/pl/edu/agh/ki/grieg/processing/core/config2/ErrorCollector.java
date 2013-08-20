package pl.edu.agh.ki.grieg.processing.core.config2;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Gathers arbitrary amount of exceptions and stores them in a list.
 * 
 * @author los
 */
class ErrorCollector implements ErrorHandler {

    private final List<Throwable> exceptions = Lists.newArrayList();

    @Override
    public void error(Throwable e) {
        exceptions.add(e);
    }
    
    public List<Throwable> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }

}
