package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ErrorCollector;

import com.google.common.collect.ImmutableList;

public class ErrorCollectorTest {

    private ErrorCollector collector;
    
    @Before
    public void setup() {
        collector = new ErrorCollector();
    }
    
    @Test
    public void worksForNoErrors() {
        assertThat(collector.getExceptions(), empty());
    }
    
    @Test
    public void worksForOneError() {
        Throwable e = new RuntimeException("It's hosed");
        collector.error(e);
        assertEquals(ImmutableList.of(e), collector.getExceptions());
    }

}
