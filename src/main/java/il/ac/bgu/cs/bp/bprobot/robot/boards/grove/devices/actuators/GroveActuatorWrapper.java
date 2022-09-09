package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.actuators;

import il.ac.bgu.cs.bp.bprobot.robot.boards.DeviceWrapper;
import lejos.hardware.port.Port;

public abstract class GroveActuatorWrapper<T> extends DeviceWrapper<T> {

  protected GroveActuatorWrapper(String name, Port port, T device) {
    super(name, port, device);
  }

  public void setBooleanValue(boolean value) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void setDoubleValue(double value) throws Exception {
    throw new UnsupportedOperationException();
  }
}