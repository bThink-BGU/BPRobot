package il.ac.bgu.cs.bp.bprobot.robot.GroveWrappers.GetWrappers;

import org.iot.raspberry.grovepi.GroveDigitalIn;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ButtonWrapperTest {

    private ButtonWrapper buttonWrapper;
    private final GroveDigitalIn button = mock(GroveDigitalIn.class);

    @Before
    public void setUp() {
        buttonWrapper = new ButtonWrapper(button);
        buttonWrapper.setLogger(mock(Logger.class));
    }

    @Test
    public void testGetReleasedButtonValue() throws IOException, InterruptedException {
        when(button.get()).thenReturn(false);
        double actual = buttonWrapper.get(1);
        assertEquals(0.0, actual, 0.01);
    }

    @Test
    public void testGetPressedButtonValue() throws IOException, InterruptedException {
        when(button.get()).thenReturn(true);
        double actual = buttonWrapper.get(1);
        assertEquals(1.0, actual, 0.01);
    }

    @Test
    public void testGetFailed() throws IOException, InterruptedException {
        when(button.get()).thenThrow(new IOException());
        doNothing().when(buttonWrapper.getLogger()).severe(anyString());
        assertNull(buttonWrapper.get(1));
    }
}