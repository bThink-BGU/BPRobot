package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.actuators;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveLed;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiPort;

import java.io.IOException;

public class Led extends GroveActuator<GroveLed> {
  public Led(String board, String name, GrovePiPort port, GrovePi grove) throws IOException {
    super(board, name, port, new GroveLed(grove, port.ordinal()));
  }

  @Override
  public void setBooleanValue(boolean value) throws IOException {
    device.set(value);
  }
}
