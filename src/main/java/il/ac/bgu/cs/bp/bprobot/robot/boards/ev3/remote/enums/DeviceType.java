package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.List;

public class DeviceType extends RemoteCode {
  public static final DeviceType DONT_CHANGE = new DeviceType("DONT_CHANGE", (byte) 0x00);
  public static final DeviceType L_MOTOR = new DeviceType("L_MOTOR", (byte) 0x07);
  public static final DeviceType M_MOTOR = new DeviceType("M_MOTOR", (byte) 0x08);
  public static final DeviceType ALL_MOTORS = new DeviceType("ALL_MOTORS", (byte) 0x0F);
  public static final DeviceType EV3_TOUCH = new DeviceType("EV3_TOUCH", (byte) 0x10);
  public static final DeviceType EV3_COLOR = new DeviceType("EV3_COLOR", (byte) 0x1D);
  public static final DeviceType EV3_ULTRASONIC = new DeviceType("EV3_ULTRASONIC", (byte) 0x1E);
  public static final DeviceType EV3_GYRO = new DeviceType("EV3_GYRO", (byte) 0x20);
  public static final DeviceType EV3_IR = new DeviceType("EV3_IR", (byte) 0x21);

  private static final List<DeviceType> values = List.of(
    L_MOTOR,
    M_MOTOR,
    EV3_TOUCH,
    EV3_COLOR,
    EV3_ULTRASONIC,
    EV3_GYRO,
    EV3_IR
  );
  private DeviceType(String name, byte code) {
    super(name, code);
  }
  public static DeviceType fromCode(byte code) {
    for (DeviceType type : values) {
      if (type.code == code) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown device type: " + code);
  }
}
