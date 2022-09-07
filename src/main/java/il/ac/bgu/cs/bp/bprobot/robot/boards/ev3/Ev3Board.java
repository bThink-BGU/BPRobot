package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.BasicMotor;
import ev3dev.hardware.EV3DevDevice;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.internals.EV3DevPort;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ev3Board extends Board {
  private static final Map<String, Port> portsMap = Map.of(
      "S1", SensorPort.S1,
      "S2", SensorPort.S2,
      "S3", SensorPort.S3,
      "S4", SensorPort.S4,
      "A", MotorPort.A,
      "B", MotorPort.B,
      "C", MotorPort.C,
      "D", MotorPort.D
  );
  private static final Logger logger = Logger.getLogger(Ev3Board.class.getName());

  public Ev3Board() {
    super(List.of("ev3dev"));
    logger.setLevel(Level.SEVERE);
  }

  @Override
  public Port getPort(String portName) {
    var port = portsMap.get(portName);
    if (port == null) throw new IllegalArgumentException("No such port " + portName);
    return port;
  }

  @Override
  public void close() {
    portDeviceMap.forEach((key, value) -> {
      if (key instanceof MotorPort) {
        if (value.device instanceof BaseRegulatedMotor) {
          ((BaseRegulatedMotor) value.device).stop();
        } else if (value.device instanceof BasicMotor) {
          ((BasicMotor) value.device).stop();
        } else {
          throw new RuntimeException("An unknown device is connected to port " + key.getName());
        }
      }
    });
    logger.info("Stopped all motors");
  }
}
