package pl.edu.agh.ki.grieg.processing.model;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActionFilterTest {
    
    private final class TestFilter extends ActionFilter {
        
        public TestFilter(Runnable action) {
            super(action);
        }

        @Override
        protected boolean shouldTrigger() {
            return shouldTrigger;
        }
    }

    private ActionFilter filter;
    @Mock private Runnable action;
    
    private boolean shouldTrigger;
    
    @Before
    public void setup() {
        filter = new TestFilter(action);
        shouldTrigger = true;
    }

    @Test
    public void doesNotFireWhenItShouldNot() {
        shouldTrigger = false;
        filter.perform();
        verifyZeroInteractions(action);
    }
    
    @Test
    public void doesFireWhenItShould() {
        filter.perform();
        verify(action).run();
        verifyNoMoreInteractions(action);
    }
    
    @Test
    public void forceFiresAlways() {
        filter.perform();
        shouldTrigger = false;
        filter.forcePerform();
        verify(action, times(2)).run();
        verifyNoMoreInteractions(action);
    }

}
