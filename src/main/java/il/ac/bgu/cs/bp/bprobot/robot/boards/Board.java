package il.ac.bgu.cs.bp.bprobot.robot.boards;

import com.google.common.base.Strings;
import lejos.hardware.port.Port;

import java.util.*;

public abstract class Board {
  protected final List<String> packages;
  protected final Map<Port, DeviceWrapper<?>> portDeviceMap = new HashMap<>();
  protected final Map<String, DeviceWrapper<?>> nicknameDeviceMap = new HashMap<>();

  protected Board() {
    packages = new ArrayList<>();
  }

  protected Board(List<String> packages) {
    this.packages = Collections.unmodifiableList(packages);
  }

  public DeviceWrapper<?> getDevice(Port port) {
    if (!portDeviceMap.containsKey(port)) {
      throw new IllegalArgumentException("Port " + port.getName() + " does not exist");
    }
    return portDeviceMap.get(port);
  }

  public DeviceWrapper<?> getDevice(String port) {
    if (!nicknameDeviceMap.containsKey(port)) {
      throw new IllegalArgumentException("Port " + port + " does not exist");
    }
    return nicknameDeviceMap.get(port);
  }
  public abstract Port getPort(String portName);
  protected abstract DeviceWrapper<?> createDeviceWrapper(String name, Port port, String type) throws Exception;
  public void putDevice(Port port, String nickname, String type, Integer mode) throws Exception {
    DeviceWrapper<?>dev = createDeviceWrapper(nickname, port, type);
    if(mode != null)
      ((SensorWrapper<?>)dev).setCurrentMode(mode);
    putDevice(port, nickname, dev);
  }

  private void putDevice(Port port, String nickname, DeviceWrapper<?> device) {
    portDeviceMap.put(port, device);
    if (!Strings.isNullOrEmpty(nickname)) {
      if (nicknameDeviceMap.containsKey(nickname)) {
        throw new IllegalArgumentException("There is already a device with the name " + nickname);
      }
      nicknameDeviceMap.put(nickname, device);
    }
  }

  public void close() {
  }
}
