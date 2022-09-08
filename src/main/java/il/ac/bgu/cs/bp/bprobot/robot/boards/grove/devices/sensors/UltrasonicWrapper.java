package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.sensors;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveUltrasonicRanger;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiPort;

public class UltrasonicWrapper extends GroveSensorWrapper<GroveUltrasonicRanger> {
    public UltrasonicWrapper(String name, GrovePiPort port, GrovePi grove){
        super(name, port, new GroveUltrasonicRanger(grove, port.ordinal()));
    }
    @Override
    protected void sample(float[] sample) throws Exception {
        sample[0] = (float) device.get();
    }
}
