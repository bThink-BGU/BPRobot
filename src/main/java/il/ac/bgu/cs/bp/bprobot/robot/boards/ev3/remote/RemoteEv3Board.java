package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote;

import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.communication.ICommunicator;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.Ev3RemoteMotor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.Ev3RemoteSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.DeviceType;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.Port;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.Ev3Protocol;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;
import il.ac.bgu.cs.bp.bprobot.util.ReflectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RemoteEv3Board extends Board<Port> {
  private static final Map<String, DeviceType> deviceMapper = Map.of(
      "EV3LargeRegulatedMotor", DeviceType.L_MOTOR,
      "EV3MediumRegulatedMotor", DeviceType.M_MOTOR,
      "EV3ColorSensor", DeviceType.EV3_COLOR,
      "EV3GyroSensor", DeviceType.EV3_GYRO,
      "EV3IRSensor", DeviceType.EV3_IR,
      "EV3TouchSensor", DeviceType.EV3_TOUCH,
      "EV3UltrasonicSensor", DeviceType.EV3_ULTRASONIC
  );
  private static final Map<String, Port> portsMap = Map.of(
      "S1", Port.S1,
      "S2", Port.S2,
      "S3", Port.S3,
      "S4", Port.S4,
      "A", Port.A,
      "B", Port.B,
      "C", Port.C,
      "D", Port.D
  );
  protected ProtocolBase mProtocol;

  public RemoteEv3Board(ICommunicator comm, String name) throws IOException {
    super(name, List.of("ev3dev"));
    mProtocol = new Ev3Protocol(comm);
    mProtocol.open();
  }

  @Override
  public Port getPort(String portName) {
    var port = portsMap.get(portName);
    if (port == null) throw new IllegalArgumentException("No such port " + portName);
    return port;
  }

  @Override
  protected Device<DeviceType> createDeviceWrapper(String nickname, Port port, String type, Object... ctorParams) throws Exception {
    Class<?> cl = ReflectionUtils.getClass(type, packages);
    var deviceType = deviceMapper.get(cl.getSimpleName());
    if (deviceType == null) throw new IllegalArgumentException("No such device type " + type);

    if (port.isSensor) {
      return new Ev3RemoteSensor(name, port, mProtocol, nickname, deviceType);
    } else if (deviceType == DeviceType.L_MOTOR) {
      return Ev3RemoteMotor.createLargeMotor(name, nickname, port, mProtocol);
    } else
      return Ev3RemoteMotor.createMediumMotor(name, nickname, port, mProtocol);
  }

  @Override
  public void close() {
    mProtocol.close();
  }
}
