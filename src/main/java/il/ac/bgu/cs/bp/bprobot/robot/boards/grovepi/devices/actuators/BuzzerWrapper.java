package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.actuators;

import com.github.yafna.raspberry.grovepi.GroveDigitalOut;
import com.github.yafna.raspberry.grovepi.GrovePi;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ActuatorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuzzerWrapper extends GroveActuatorWrapper<GroveDigitalOut> {
    public BuzzerWrapper(String name, GrovePiPort port, GrovePi grove) throws IOException {
        super(name, port, new GroveDigitalOut(grove,port.ordinal()));
    }

    @Override
    public void setBooleanValue(boolean value) throws Exception {
        device.set(value);
    }
}
