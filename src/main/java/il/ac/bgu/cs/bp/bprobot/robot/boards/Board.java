package il.ac.bgu.cs.bp.bprobot.robot.boards;

import com.google.common.base.Strings;
import lejos.hardware.port.Port;
import org.mockito.Mockito;

import java.util.*;

public abstract class Board<P extends Port> {
  public final String name;
  protected final boolean isMock;
  protected final List<String> packages;
  protected final Map<P, Device> portDeviceMap = new HashMap<>();
  protected final Map<String, Device> nicknameDeviceMap = new HashMap<>();

  protected Board(String name) {
    this(name, List.of(), false);
  }

  protected Board(String name, boolean isMock) {
    this(name, List.of(), isMock);
  }

  protected Board(String name, List<String> packages) {
    this(name, Collections.unmodifiableList(packages), false);
  }

  protected Board(String name, List<String> packages, boolean isMock) {
    this.name = name;
    this.isMock = isMock;
    this.packages = Collections.unmodifiableList(packages);
  }

  public Device getDevice(P port) {
    if (!portDeviceMap.containsKey(port)) {
      throw new IllegalArgumentException("Port " + port.getName() + " does not exist");
    }
    return portDeviceMap.get(port);
  }

  public Device getDevice(String port) {
    return nicknameDeviceMap.get(port);
  }

  public abstract P getPort(String portName);

  /**
   * Create a new device wrapper for the given type.
   *
   * @param nickname The nickname of the device.
   * @param port     The port of the device.
   * @param type     The class type of the device. The type can be either full name (i.e., including the full package name)
   *                 or a simple name (i.e., without the package name). If the type is a simple name, the class will be
   *                 searched in the packages list ({@link #packages}).
   * @return The device wrapper.
   * @throws Exception is thrown if the device cannot be created.
   */
  protected abstract Device createDeviceWrapper(String nickname, P port, String type, Object... ctorParams) throws Exception;

  public Device putDevice(P port, String nickname, String type, Integer mode, Object... ctorParams) throws Exception {
    Device dev = createDeviceWrapper(nickname, port, type, ctorParams);
    if (mode != null) {
      ((Sensor) dev).setCurrentMode(mode);
      if (isMock) {
        Mockito.when(((Sensor) dev).getCurrentMode()).thenReturn(mode);
      }
    }
    putDevice(port, nickname, dev);
    return dev;
  }

  public Device putDevice(P port, String nickname, String type, String mode, Object... ctorParams) throws Exception {
    Device dev = createDeviceWrapper(nickname, port, type, ctorParams);
    if (mode != null) {
      ((Sensor) dev).setCurrentMode(mode);
      if (isMock) {
        Mockito.when(((Sensor) dev).getCurrentMode()).thenReturn(0);
      }
    }
    putDevice(port, nickname, dev);
    return dev;
  }

  private void putDevice(P port, String nickname, Device device) {
    portDeviceMap.put(port, device);
    if (!Strings.isNullOrEmpty(nickname)) {
      if (nicknameDeviceMap.containsKey(nickname)) {
        throw new IllegalArgumentException("There is already a device with the name " + nickname);
      }
      nicknameDeviceMap.put(nickname, device);
    }
    if (!port.getName().equals(nickname)) {
      nicknameDeviceMap.put(port.getName(), device);
    }
  }

  public void close() {
  }
}
