package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.sensors;

import il.ac.bgu.cs.bp.bprobot.robot.boards.GenericSensorMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import lejos.hardware.port.Port;

public abstract class GroveSensorWrapper<T> extends SensorWrapper<T> {

  protected GroveSensorWrapper(String name, Port port, T device, GenericSensorMode... modes) {
    super(name, port, device, modes.length == 0 ? new GenericSensorMode[]{new GenericSensorMode()} : modes);
  }
}
