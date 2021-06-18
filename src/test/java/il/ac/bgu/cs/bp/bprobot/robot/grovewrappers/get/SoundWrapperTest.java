package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveSoundSensor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SoundWrapperTest {

    private SoundWrapper soundWrapper;
    private final GroveSoundSensor soundSensor = mock(GroveSoundSensor.class);

    @Before
    public void setUp() {
        soundWrapper = new SoundWrapper(soundSensor);
        soundWrapper.setLogger(mock(Logger.class));
    }

    @Test
    public void testGetSound() throws IOException {
        when(soundSensor.get()).thenReturn(0.5);
        double actual = soundWrapper.get(0);
        assertEquals(0.5, actual, 0.01);
    }

    @Test
    public void testGetFailed() throws IOException {
        when(soundSensor.get()).thenThrow(new IOException());
        doNothing().when(soundWrapper.getLogger()).severe(anyString());
        assertNull(soundWrapper.get(0));
    }
}