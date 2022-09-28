package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.sensors;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveSoundSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiPort;

import java.io.IOException;

public class SoundWrapper extends SensorWrapper<GroveSoundSensor> {

    public SoundWrapper(String board, String name, GrovePiPort port, GrovePi grove) throws IOException {
        super(board, name, port, new GroveSoundSensor(grove, port.ordinal()));
    }

    @Override
    protected void sample(float[] sample) throws Exception {
        double ret = device.get();
        sample[0] = (float) ret;
    }
}
