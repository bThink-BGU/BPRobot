package il.ac.bgu.cs.bp.bprobot.robot.GroveWrappers.SetWrappers;

import org.iot.raspberry.grovepi.GroveDigitalOut;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@PrepareForTest({GroveDigitalOut.class})
public class BuzzerWrapperTest {

    private BuzzerWrapper buzzerWrapperMock;
    private GroveDigitalOut buzzerMock;

    @Before
    public void setUp() {
        buzzerMock = Mockito.mock(GroveDigitalOut.class);
        buzzerWrapperMock = Mockito.spy(new BuzzerWrapper(buzzerMock));
        buzzerWrapperMock.setLogger(Mockito.mock(Logger.class));
    }

    @Test
    public void testSetFailed() throws IOException {
        Mockito.doThrow(new IOException()).when(buzzerMock).set(true);
        disableLogger();
        boolean actual = buzzerWrapperMock.set(true);
        assertFalse(actual);
    }

    private void disableLogger() {
        Logger logger = buzzerWrapperMock.getLogger();
        Mockito.doNothing().when(logger).severe(anyString());
    }

    @Test
    public void testSetTrueSuccess() throws IOException {
        Mockito.doNothing().when(buzzerMock).set(true);
        boolean actual = buzzerWrapperMock.set(true);
        assertTrue(actual);
    }

    @Test
    public void testSetFalseSuccess() throws IOException {
        Mockito.doNothing().when(buzzerMock).set(false);
        boolean actual = buzzerWrapperMock.set(false);
        assertTrue(actual);
    }
}