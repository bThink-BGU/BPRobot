package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveUltrasonicRanger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UltrasonicWrapperTest {

    private UltrasonicWrapper ultrasonicWrapper;
    private final GroveUltrasonicRanger ultrasonicRanger = mock(GroveUltrasonicRanger.class);

    @BeforeEach
    public void setUp() {
        ultrasonicWrapper = new UltrasonicWrapper(ultrasonicRanger);
        ultrasonicWrapper.setLogger(mock(Logger.class));
    }

    @Test
    public void testGetUltrasonic() throws IOException {
        when(ultrasonicRanger.get()).thenReturn(0.5);
        double actual = ultrasonicWrapper.get(1);
        assertEquals(0.5, actual, 0.01);
    }

    @Test
    public void testGetFailed() throws IOException {
        when(ultrasonicRanger.get()).thenThrow(new IOException());
        doNothing().when(ultrasonicWrapper.getLogger()).severe(anyString());
        assertNull(ultrasonicWrapper.get(1));
    }
}
