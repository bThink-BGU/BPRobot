package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote;

import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.communication.ICommunicator;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.Ev3RemoteSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.port.Ev3InputPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.port.Ev3OutputPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.Ev3Protocol;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;
import il.ac.bgu.cs.bp.bprobot.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RemoteEv3Board extends Board<DevicePort> {
  private static final Map<String, DevicePort> portsMap = Map.of(
      "S1", Ev3InputPort.PORT_1,
      "S2", Ev3InputPort.PORT_2,
      "S3", Ev3InputPort.PORT_3,
      "S4", Ev3InputPort.PORT_4,
      "A", Ev3OutputPort.PORT_A,
      "B", Ev3OutputPort.PORT_B,
      "C", Ev3OutputPort.PORT_C,
      "D", Ev3OutputPort.PORT_D
  );
  protected ProtocolBase mProtocol;

  public RemoteEv3Board(ICommunicator comm, String name) throws IOException {
    super(name, List.of("il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices"));
    mProtocol = new Ev3Protocol(comm);
    mProtocol.open();
  }

  @Override
  public DevicePort getPort(String portName) {
    var port = portsMap.get(portName);
    if (port == null) throw new IllegalArgumentException("No such port " + portName);
    return port;
  }

  @Override
  protected Device createDeviceWrapper(String nickname, DevicePort port, String type, Object... ctorParams) throws Exception {
    Class<?> cl = ReflectionUtils.getClass(type, packages);
    Constructor<?> ctor;
    Object device;
    Class<?>[] ctorParamsTypes = new Class<?>[0];
    ctorParams = new Object[0];
    try {
      ctor = cl.getConstructor(ctorParamsTypes);
    } catch (NoSuchMethodException ignored2) {
      throw new IllegalArgumentException("Could not find an appropriate constructor for class " + cl.getName());
    }

    try {
      device = ctor.newInstance(ctorParams);
    } catch (Exception e) {
      throw new InstantiationException("Could not create class " + cl.getSimpleName() + " with parameters " + Arrays.toString(ctorParamsTypes) + ". Do we run on an EV3 device?");
    }
    if (port instanceof Ev3InputPort) {
      return (Ev3RemoteSensor) device;
    }
    return new Device(name, nickname, port, device);
  }

  @Override
  public void close() {
    mProtocol.close();
  }
}
