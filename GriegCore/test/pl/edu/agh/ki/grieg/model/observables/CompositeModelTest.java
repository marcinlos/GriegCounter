package pl.edu.agh.ki.grieg.model.observables;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompositeModelTest {

    @Mock private Listener<String> leftListener;
    @Mock private Listener<Integer> leftTopListener;
    @Mock private Listener<Integer> leftBottomListener;
    @Mock private Listener<Integer> rightTopListener;

    private CompositeModel<Void> model;

    private CompositeModel<String> left;
    private CompositeModel<String> right;

    private SimpleModel<Integer> leftTop;
    private SimpleModel<Integer> leftBottom;
    private SimpleModel<Integer> rightTop;

    @Before
    public void setup() {
        model = CompositeModel.of(Void.class);

        left = CompositeModel.of(String.class);
        right = CompositeModel.of(String.class);

        leftTop = SimpleModel.of(Integer.class);
        leftBottom = SimpleModel.of(Integer.class);
        rightTop = SimpleModel.of(Integer.class);

        model.addModel("left", left);
        model.addModel("right", right);

        left.addModel("top", leftTop);
        left.addModel("bottom", leftBottom);
        right.addModel("top", rightTop);

    }

    private void wireListeners() {
        model.addListener("left", leftListener, String.class);

        model.addListener("left.top", leftTopListener, Integer.class);
        model.addListener("left.bottom", leftBottomListener, Integer.class);
        model.addListener("right.top", rightTopListener, Integer.class);
    }
    
    @Test(expected = InvalidModelNameException.class)
    public void addListenerGivenInvalidNameThrows() {
        left.addModel("adf_Bd  f&*", leftTop);
    }

    @Test
    public void addListenerGivenEmptyPathAddsListenerToTheModel() {
        leftTop.addListener("", leftTopListener, Integer.class);
        leftTop.update(666);
        verify(leftTopListener).update(666);
    }

    @Test
    public void hasChildGivenEmptyPathReturnsTrue() {
        assertTrue(model.hasChild(""));
        assertTrue(leftTop.hasChild(""));
    }

    @Test
    public void getChildGivenEmptyPathReturnsThis() {
        assertSame(model, model.getChild(""));
    }

    @Test
    public void getChildGivenEmptyPathAndCorrectTypeReturnsThis() {
        assertSame(model, model.getChild("", Void.class));
    }

    @Test(expected = InvalidModelTypeException.class)
    public void getGivenEmptyPathAndInvalidTypeThrows() {
        model.getChild("", List.class);
    }

    @Test
    public void canFindDirectChild() {
        assertTrue(model.hasChild("left"));
    }

    @Test
    public void canGetDirectChild() {
        assertSame(left, model.getChild("left"));
    }

    @Test
    public void canGetDirectChildWithType() {
        assertSame(left, model.getChild("left", String.class));
    }

    @Test(expected = InvalidModelTypeException.class)
    public void getDirectChildWithInvalidTypeThrows() {
        model.getChild("left", double[][].class);
    }

    @Test
    public void doesNotFindNonexistantChild() {
        assertFalse(model.hasChild("some.other"));
    }

    @Test
    public void getChildGivenNonexistantChildPathReturnsNull() {
        assertNull(model.getChild("some.other"));
    }

    @Test
    public void canFindChild() {
        assertTrue(model.hasChild("left.top"));
        assertTrue(model.hasChild("left.bottom"));
        assertTrue(model.hasChild("right.top"));
    }

    @Test
    public void canGetChild() {
        assertSame(leftTop, model.getChild("left.top"));
        assertSame(leftBottom, model.getChild("left.bottom"));
        assertSame(rightTop, model.getChild("right.top"));
    }

    @Test
    public void canGetChildWithType() {
        assertSame(leftTop, model.getChild("left.top", Integer.class));
        assertSame(leftBottom, model.getChild("left.bottom", Integer.class));
        assertSame(rightTop, model.getChild("right.top", Integer.class));
    }

    @Test(expected = InvalidModelTypeException.class)
    public void getChildGivenInvalidTypeThrows() {
        model.getChild("left.top", String.class);
    }

    @Test(expected = InvalidModelTypeException.class)
    public void addListenerWithWrongTypeThrows() {
        model.addListener("left.top", leftListener, String.class);
    }

    @Test
    public void listenersReceiveNotifications() {
        wireListeners();

        left.update("left value");
        leftBottom.update(666);
        leftTop.update(999);
        rightTop.update(-100);

        verify(leftListener).update("left value");
        verify(leftBottomListener).update(666);
        verify(leftTopListener).update(999);
        verify(rightTopListener).update(-100);

        verifyNoMoreInteractions(leftListener, leftBottomListener,
                leftTopListener, rightTopListener);
    }
    
    @Test
    public void removedListenersDontReceiveNotifications() {
        wireListeners();
        model.removeListener("left", leftListener);
        leftBottom.removeListener("", leftBottomListener);
        
        left.update("left value");
        leftBottom.update(666);
        leftTop.update(999);
        rightTop.update(-100);
        
        verify(leftTopListener).update(999);
        verify(rightTopListener).update(-100);
        
        verifyNoMoreInteractions(leftTopListener, rightTopListener);
        verifyZeroInteractions(leftListener, leftBottomListener);
    }
    
    @Test(expected = NoSuchModelException.class)
    public void removedSubmodelsAreRemoved() {
        model.removeModel("left");
        model.addListener("left", leftListener, String.class);
    }
}
