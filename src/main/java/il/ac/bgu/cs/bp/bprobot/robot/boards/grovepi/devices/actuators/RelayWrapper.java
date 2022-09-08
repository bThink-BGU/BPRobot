package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.actuators;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveLed;
import com.github.yafna.raspberry.grovepi.devices.GroveRelay;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ActuatorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelayWrapper extends GroveActuatorWrapper<GroveRelay> {
  public RelayWrapper(String name, GrovePiPort port, GrovePi grove) throws IOException {
    super(name, port, new GroveRelay(grove, port.ordinal()));
  }

  @Override
  public void setBooleanValue(boolean value) throws IOException {
    device.set(value);
  }
}
