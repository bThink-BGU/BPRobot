package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.set;

import org.iot.raspberry.grovepi.devices.GroveRelay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@PrepareForTest({GroveRelay.class})
public class RelayWrapperTest {

    private RelayWrapper relayWrapperMock;
    private GroveRelay relayMock;

    @BeforeEach
    public void setUp() {
        relayMock = Mockito.mock(GroveRelay.class);
        relayWrapperMock = Mockito.spy(new RelayWrapper(relayMock));
        relayWrapperMock.setLogger(Mockito.mock(Logger.class));
    }

    @Test
    public void testSetFailed() throws IOException {
        Mockito.doThrow(new IOException()).when(relayMock).set(true);
        disableLogger();
        boolean actual = relayWrapperMock.set(true);
        assertFalse(actual);
    }

    private void disableLogger() {
        Logger logger = relayWrapperMock.getLogger();
        Mockito.doNothing().when(logger).severe(anyString());
    }

    @Test
    public void testSetTrueSuccess() throws IOException {
        Mockito.doNothing().when(relayMock).set(true);
        boolean actual = relayWrapperMock.set(true);
        assertTrue(actual);
    }

    @Test
    public void testSetFalseSuccess() throws IOException {
        Mockito.doNothing().when(relayMock).set(false);
        boolean actual = relayWrapperMock.set(false);
        assertTrue(actual);
    }
}
