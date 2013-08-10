package pl.edu.agh.ki.grieg.model.observables;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleModelTest {

    @Mock private Listener<String> listener1;
    
    @Mock private Listener<String> listener2;
    
    private SimpleModel<String> model;
    
    @Before
    public void setup() {
        model = new SimpleModel<String>(String.class);
    }
    
    @Test
    public void hasNoChildren() {
        assertFalse(model.hasChild("some.path"));
    }
    
    @Test
    public void getChildReturnsNull() {
        assertNull(model.getChild("some.path"));
        assertNull(model.getChild("some.path", int[].class));
    }
    
    @Test
    public void getChildrenReturnsEmptyMap() {
        assertThat(model.getChildren().keySet(), empty());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void cannotAddListenersByPath() {
        model.addListener("some.path", listener1, String.class);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void cannotRemoveListenerByPath() {
        model.removeListener("some.path", listener1);
    }

}
