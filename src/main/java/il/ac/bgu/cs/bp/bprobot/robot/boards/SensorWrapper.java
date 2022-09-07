package il.ac.bgu.cs.bp.bprobot.robot.boards;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorModes;

public abstract class SensorWrapper<T> extends DeviceWrapper<T> implements SensorModes {
  protected SensorWrapper(String name, Port port, T device) {
    super(name, port, device);
  }
}
