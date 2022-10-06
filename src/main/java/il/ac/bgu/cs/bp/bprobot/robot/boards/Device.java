package il.ac.bgu.cs.bp.bprobot.robot.boards;

import lejos.hardware.port.Port;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Device<T> {
  public final String board;
  public final String name;
  public final Port port;
  public final T device;

  public String getName() {
    return name;
  }

  public Device(String board, String name, Port port, T device) {
    this.board = board;
    this.name = name;
    this.port = port;
    this.device = device == null? (T)this: device;
  }

  protected T getDevice() {
    return device;
  }

  private Method getMethod(String name, Object... params) throws NoSuchMethodException {
    return device.getClass().getMethod(name, Arrays.stream(params).map(Object::getClass).toArray(Class[]::new));
  }

  public void runVoidMethod(String name, Object... params) throws NoSuchMethodException {
    runMethod(name, params);
  }

  public <V> V runMethod(String name, Object... params) throws NoSuchMethodException {
    try {
      return (V) getMethod(name, params).invoke(device, params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Failed to run method " + name, e);
    }
  }
}
