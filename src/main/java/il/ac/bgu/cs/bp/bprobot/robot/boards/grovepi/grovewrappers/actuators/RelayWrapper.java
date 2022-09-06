package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.actuators;

import com.github.yafna.raspberry.grovepi.devices.GroveRelay;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelayWrapper implements GroveSensorActuatorWrapper {
  private Logger logger = Logger.getLogger(RelayWrapper.class.getName());
  private final GroveRelay relay;

  public RelayWrapper(GroveRelay relay) {
    this.relay = relay;
    logger.setLevel(Level.SEVERE);
  }

  @Override
  public boolean set(boolean value) {
    try {
      relay.set(value);
      return true;
    } catch (IOException e) {
      logger.severe("Error when writing data to port");
      return false;
    }
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public Logger getLogger() {
    return this.logger;
  }
}
