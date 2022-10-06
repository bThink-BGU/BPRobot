package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.sensors;

import com.github.yafna.raspberry.grovepi.GroveDigitalIn;
import com.github.yafna.raspberry.grovepi.GrovePi;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Sensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiPort;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Button extends Sensor<GroveDigitalIn> {
  private static final Logger logger = Logger.getLogger(Button.class.getName());

  Button(String board, String name, GrovePiPort port, GrovePi grove) throws IOException {
    super(board, name, port, new GroveDigitalIn(grove, port.ordinal()));
    logger.setLevel(Level.SEVERE);
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    sample[0] = device.get() ? 1 : 0;
  }
}
