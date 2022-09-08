package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.sensors;

import com.github.yafna.raspberry.grovepi.GroveDigitalIn;
import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveSoundSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundWrapper extends GroveSensorWrapper<GroveSoundSensor> {

    public SoundWrapper(String name, GrovePiPort port, GrovePi grove) throws IOException {
        super(name, port, new GroveSoundSensor(grove, port.ordinal()));
    }

    @Override
    protected void sample(float[] sample) throws Exception {
        double ret = device.get();
        sample[0] = (float) ret;
    }
}
