package pl.edu.agh.ki.grieg.processing.model;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({FixedRateActionFilter.class})
public class FixedRateActionFilterTest {
    
    private static final long SEC = 1000000000L;
    
    @Mock private Runnable action;
    private FixedRateActionFilter filter;
    
    
    @Before
    public void setup() {
        initMocks(FixedRateActionFilterTest.class);
        mockStatic(System.class);
        when(System.nanoTime()).thenReturn(SEC, 2*SEC, 5*SEC);
    }
    
    @Test
    public void worksCorrectly() {
        filter = new FixedRateActionFilter(action, TimeUnit.SECONDS, 3);
        filter.perform();
        filter.perform();
        filter.perform();
        verify(action).run();
        verifyNoMoreInteractions(action);
    }

}
