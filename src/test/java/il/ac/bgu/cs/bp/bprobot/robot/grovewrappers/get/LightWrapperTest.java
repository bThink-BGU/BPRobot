package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveLightSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LightWrapperTest {

    private LightWrapper lightWrapper;
    private final GroveLightSensor lightSensor = mock(GroveLightSensor.class);

    @BeforeEach
    public void setUp() {
        lightWrapper = new LightWrapper(lightSensor);
        lightWrapper.setLogger(mock(Logger.class));
    }

    @Test
    public void testGetLightValue() throws IOException {
        when(lightSensor.get()).thenReturn(0.5);
        double actual = lightWrapper.get(0);
        assertEquals(0.5, actual, 0.01);
    }

    @Test
    public void testGetFailed() throws IOException {
        when(lightSensor.get()).thenThrow(new IOException());
        doNothing().when(lightWrapper.getLogger()).severe(anyString());
        assertNull(lightWrapper.get(0));
    }
}