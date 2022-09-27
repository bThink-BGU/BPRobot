package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.BasicMotor;
import ev3dev.sensors.BaseSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.DeviceWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.devices.sensors.Ev3BaseSensorWrapper;
import il.ac.bgu.cs.bp.bprobot.util.ReflectionUtils;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.internals.EV3DevPort;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ev3Board extends Board<EV3DevPort> {
  private static final Map<String, EV3DevPort> portsMap = Map.of(
      "S1", (EV3DevPort) SensorPort.S1,
      "S2", (EV3DevPort) SensorPort.S2,
      "S3", (EV3DevPort) SensorPort.S3,
      "S4", (EV3DevPort) SensorPort.S4,
      "A", (EV3DevPort) MotorPort.A,
      "B", (EV3DevPort) MotorPort.B,
      "C", (EV3DevPort) MotorPort.C,
      "D", (EV3DevPort) MotorPort.D
  );
  private static final Logger logger = Logger.getLogger(Ev3Board.class.getName());

  public Ev3Board(boolean isMock) {
    super(List.of("ev3dev"), isMock);
    logger.setLevel(Level.SEVERE);
  }

  @Override
  public EV3DevPort getPort(String portName) {
    var port = portsMap.get(portName);
    if (port == null) throw new IllegalArgumentException("No such port " + portName);
    return port;
  }

  @Override
  protected DeviceWrapper<?> createDeviceWrapper(String nickname, EV3DevPort port, String type, Object... ctorParams) throws Exception {
    Class<?> cl = ReflectionUtils.getClass(type, packages);
    Constructor<?> ctor;
    Object device = null;
    Class<?>[] ctorParamsTypes = new Class<?>[]{Port.class};
    ctorParams = new Object[]{port};
    try {
      ctor = cl.getConstructor(ctorParamsTypes);
    } catch (NoSuchMethodException ignored) {
      try {
        ctor = cl.getConstructor();
        ctorParamsTypes = new Class<?>[0];
        ctorParams = new Object[0];
      } catch (NoSuchMethodException ignored2) {
        throw new IllegalArgumentException("Could not find an appropriate constructor for class " + cl.getName());
      }
    }
    if (!isMock) {
      try {
        device = ctor.newInstance(ctorParams);
      } catch (Exception e) {
        throw new InstantiationException("Could not create class " + cl.getSimpleName() + " with parameters " + Arrays.toString(ctorParamsTypes) + ". Do we run on an EV3 device?");
      }
    } else {
      device = Mockito.mock(cl);
      if (BaseSensor.class.isAssignableFrom(cl)) {
        Mockito.doAnswer(invocation -> {
          var values = invocation.<float[]>getArgument(0);
          Arrays.fill(values, 0);
          return null;
        }).when((BaseSensor) device).fetchSample(Mockito.any(), Mockito.anyInt());
      }
    }
    if (BaseSensor.class.isAssignableFrom(cl)) {
//    if (device instanceof BaseSensor) {
      return new Ev3BaseSensorWrapper(nickname, port, (BaseSensor) device);
    }
    return new DeviceWrapper<>(nickname, port, device);
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
