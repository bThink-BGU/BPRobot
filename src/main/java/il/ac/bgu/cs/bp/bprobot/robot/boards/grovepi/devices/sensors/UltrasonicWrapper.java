package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.sensors;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveTemperatureAndHumiditySensor;
import com.github.yafna.raspberry.grovepi.devices.GroveUltrasonicRanger;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UltrasonicWrapper extends GroveSensorWrapper<GroveUltrasonicRanger> {
    public UltrasonicWrapper(String name, GrovePiPort port, GrovePi grove){
        super(name, port, new GroveUltrasonicRanger(grove, port.ordinal()));
    }
    @Override
    protected void sample(float[] sample) throws Exception {
        sample[0] = (float) device.get();
    }
}
