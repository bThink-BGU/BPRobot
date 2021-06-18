package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveTemperatureAndHumiditySensor;
import org.iot.raspberry.grovepi.devices.GroveTemperatureAndHumidityValue;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TemperatureWrapperTest {

    private TemperatureWrapper temperatureWrapper;
    private final GroveTemperatureAndHumiditySensor temperatureAndHumiditySensor = mock(GroveTemperatureAndHumiditySensor.class);

    @Before
    public void setUp() {
        temperatureWrapper = new TemperatureWrapper(temperatureAndHumiditySensor);
        temperatureWrapper.setLogger(mock(Logger.class));
    }

    @Test
    public void testGetTemperature() throws IOException {
        double temperature = 30.5;
        when(temperatureAndHumiditySensor.get()).thenReturn(mock(GroveTemperatureAndHumidityValue.class));
        GroveTemperatureAndHumidityValue result = temperatureAndHumiditySensor.get();
        when(result.getTemperature()).thenReturn(30.5);
        assertEquals(temperature, temperatureWrapper.get(0), 0.01);
    }

    @Test
    public void testGetHumidity() throws IOException {
        double humidity = 20.5;
        when(temperatureAndHumiditySensor.get()).thenReturn(mock(GroveTemperatureAndHumidityValue.class));
        GroveTemperatureAndHumidityValue result = temperatureAndHumiditySensor.get();
        when(result.getHumidity()).thenReturn(20.5);
        assertEquals(humidity, temperatureWrapper.get(1), 0.01);
    }

    @Test
    public void testGetFailed() throws IOException {
        when(temperatureAndHumiditySensor.get()).thenThrow(new IOException());
        doNothing().when(temperatureWrapper.getLogger()).severe(anyString());
        assertNull(temperatureWrapper.get(0));
    }
}