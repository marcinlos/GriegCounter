package pl.edu.agh.ki.grieg.model.observables;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractModelTest {

    @Mock private Listener<String> listener1;
    @Mock private Listener<String> listener2;

    private AbstractModel<String> model;

    @Before
    public void setup() {
        model = new StubModel<String>(String.class);
    }

    private void addListeners() {
        model.addListener(listener1);
        model.addListener(listener2);
    }

    @Test
    public void oneListenerReceivesNotification() {
        model.addListener(listener1);

        String value = "Yeah, it works";
        model.update(value);

        verify(listener1).update(value);
    }

    @Test
    public void twoListenersReceiveNotification() {
        addListeners();

        String value = "Yeah, two work too";
        model.update(value);

        verify(listener1).update(value);
        verify(listener2).update(value);
    }

    @Test
    public void listenersReceiveNotificationAfterUpdateWithoutValue() {
        String value = "Yeah, no new value works as well";
        model.update(value);
        addListeners();

        model.update();

        verify(listener1).update(value);
        verify(listener2).update(value);
    }

    @Test
    public void getDataReturnsPreviouslySetValue() {
        String value = "Some string";
        model.update(value);
        assertEquals(value, model.getData());
    }

    @Test
    public void getDataTypeReturnsCorrectClass() {
        assertEquals(String.class, model.getDataType());
    }

    @Test
    public void removedListenersDontReceiveNotifications() {
        addListeners();
        model.removeListener(listener1);

        String value = "Somer value";
        model.update(value);

        verify(listener1, never()).update(anyString());
        verify(listener2).update(value);
    }

    private class StubModel<T> extends AbstractModel<T> {

        public StubModel(Class<? extends T> dataType) {
            super(dataType);
        }

        @Override
        public Map<String, Model<?>> getChildren() {
            throw new AbstractMethodError();
        }
    }

}
