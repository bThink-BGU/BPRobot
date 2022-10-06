package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.sensors;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveUltrasonicRanger;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Sensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiPort;

public class Ultrasonic extends Sensor<GroveUltrasonicRanger> {
    public Ultrasonic(String board, String name, GrovePiPort port, GrovePi grove){
        super(board, name, port, new GroveUltrasonicRanger(grove, port.ordinal()));
    }
    @Override
    protected void sample(float[] sample) throws Exception {
        sample[0] = (float) device.get();
    }
}
