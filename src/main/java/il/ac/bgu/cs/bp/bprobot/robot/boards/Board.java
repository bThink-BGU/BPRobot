package il.ac.bgu.cs.bp.bprobot.robot.boards;

import com.google.common.base.Strings;
import il.ac.bgu.cs.bp.bprobot.util.ReflectionUtils;
import lejos.hardware.port.Port;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Board {
  private final List<String> packages;
  protected final Map<Port, DeviceWrapper<?>> portDeviceMap = new HashMap<>();
  protected final Map<String, DeviceWrapper<?>> nameDeviceMap = new HashMap<>();

  protected Board() {
    packages = new ArrayList<>();
  }

  protected Board(List<String> packages) {
    this.packages = packages;
  }

  public DeviceWrapper<?> getDevice(Port port) {
    if (!portDeviceMap.containsKey(port)) {
      throw new IllegalArgumentException("Port " + port.getName() + " does not exist");
    }
    return portDeviceMap.get(port);
  }

  public DeviceWrapper<?> getDevice(String port) {
    if (!nameDeviceMap.containsKey(port)) {
      throw new IllegalArgumentException("Port " + port + " does not exist");
    }
    return nameDeviceMap.get(port);
  }

  public abstract Port getPort(String portName);

  public void putDevice(Port port, String deviceName, String type, Integer mode) {
    try {
      var dev = ReflectionUtils.<DeviceWrapper<?>>create(type, packages);
      if(mode != null) {
        ((SensorWrapper<?>)dev).setCurrentMode(mode);
      }
      putDevice(port, deviceName, dev);
    } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Failed to create device " + deviceName, e);
    }
  }

  public void putDevice(Port port, String name, DeviceWrapper<?> device) {
    portDeviceMap.put(port, device);
    if (!Strings.isNullOrEmpty(name)) {
      if (nameDeviceMap.containsKey(name)) {
        throw new IllegalArgumentException("There is already a device with the name " + name);
      }
      nameDeviceMap.put(name, device);
    }
  }

  public void close() {
  }
}
