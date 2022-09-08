package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.sensors;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveSoundSensor;
import com.github.yafna.raspberry.grovepi.devices.GroveTemperatureAndHumiditySensor;
import com.github.yafna.raspberry.grovepi.devices.GroveTemperatureAndHumidityValue;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemperatureWrapper extends GroveSensorWrapper<GroveTemperatureAndHumiditySensor> {
    public TemperatureWrapper(String name, GrovePiPort port, GrovePi grove, GroveTemperatureAndHumiditySensor.Type type) {
        super(name, port, new GroveTemperatureAndHumiditySensor(grove, port.ordinal(), type),
            new GenericGroveMode(2, "DEFAULT")
        );
    }
    @Override
    protected void sample(float[] sample) throws Exception {
        var res = device.get();
        sample[0] = (float) res.getTemperature();
        sample[1] = (float) res.getHumidity();
    }
}
