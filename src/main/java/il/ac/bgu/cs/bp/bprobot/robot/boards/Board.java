package il.ac.bgu.cs.bp.bprobot.robot.boards;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class Board<Port extends lejos.hardware.port.Port, Device> {
  protected final Map<Port, Device> devices;
  private long delay = 0;

  protected Board() {
    this.devices = new HashMap<>();
  }

  protected Board(Map<Port, Device> devices) {
    this.devices = devices;
  }

  public Device getDevice(Port port) {
    if (!devices.containsKey(port)) {
      throw new IllegalArgumentException("Port " + port.getName() + " does not exist");
    }
    return devices.get(port);
  }

  public Port getPort(String portName) {
    return devices.keySet().stream().parallel()
        .filter(p->p.getName().equals(portName))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("No such port "+portName));
  }

  public void putDevice(Port port, Device device) {
    devices.put(port, device);
  }

  public void close() {  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  protected void delay() {
    if (delay > 0) {
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
