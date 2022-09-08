package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.sensors;

import com.github.yafna.raspberry.grovepi.GroveDigitalIn;
import com.github.yafna.raspberry.grovepi.GrovePi;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ButtonWrapper extends GroveSensorWrapper<GroveDigitalIn> {
  private static final Logger logger = Logger.getLogger(ButtonWrapper.class.getName());

  ButtonWrapper(String name, GrovePiPort port, GrovePi grove) throws IOException {
    super(name, port, new GroveDigitalIn(grove, port.ordinal()));
    logger.setLevel(Level.SEVERE);
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    sample[0] = device.get() ? 1 : 0;
  }
}
