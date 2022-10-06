package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.actuators;

import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import lejos.hardware.port.Port;

public abstract class GroveActuator<T> extends Device<T> {

  protected GroveActuator(String board, String name, Port port, T device) {
    super(board, name, port, device);
  }

  public void setBooleanValue(boolean value) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void setDoubleValue(double value) throws Exception {
    throw new UnsupportedOperationException();
  }
}