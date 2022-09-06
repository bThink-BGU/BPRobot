package il.ac.bgu.cs.bp.bprobot.robot.boards;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.BasicMotor;
import ev3dev.hardware.EV3DevDevice;
import lejos.hardware.port.MotorPort;
import lejos.internals.EV3DevPort;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ev3Board extends Board<EV3DevPort, EV3DevDevice> {

  private static final Logger logger = Logger.getLogger(Ev3Board.class.getName());

  public Ev3Board() {
    super();
    logger.setLevel(Level.SEVERE);
  }

  public Ev3Board(Map<EV3DevPort, EV3DevDevice> devices) {
    super(devices);
    logger.setLevel(Level.SEVERE);
  }

  @Override
  public void close() {
    devices.forEach((key, value) -> {
      if (key instanceof MotorPort) {
        if (value instanceof BaseRegulatedMotor) {
          ((BaseRegulatedMotor) value).stop();
        } else if (value instanceof BasicMotor) {
          ((BasicMotor) value).stop();
        } else {
          throw new RuntimeException("An unknown device is connected to port " + key.getName());
        }
      }
    });
    logger.info("Stopped all motors");
  }
}
