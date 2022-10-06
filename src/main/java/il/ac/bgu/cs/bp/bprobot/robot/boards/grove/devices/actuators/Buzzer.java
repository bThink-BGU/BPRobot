package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.actuators;

import com.github.yafna.raspberry.grovepi.GroveDigitalOut;
import com.github.yafna.raspberry.grovepi.GrovePi;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiPort;

import java.io.IOException;

public class Buzzer extends GroveActuator<GroveDigitalOut> {
    public Buzzer(String board, String name, GrovePiPort port, GrovePi grove) throws IOException {
        super(board, name, port, new GroveDigitalOut(grove,port.ordinal()));
    }

    @Override
    public void setBooleanValue(boolean value) throws Exception {
        device.set(value);
    }
}
