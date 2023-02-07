package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.DeviceType.*;

/**
 * All device modes. More explanations at <a href="https://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-buster/sensor_data.html#lego-ev3-us">ev3dev device modes</a>
 */
public class DeviceMode extends RemoteCode {
  public static final byte DONT_CHANGE = (byte) -0x01;
  public final static Map<DeviceType, List<DeviceMode>> DEVICE_MODES = Map.of(
    L_MOTOR, List.of(
      new DeviceMode(L_MOTOR, "L-MOTOR-DEG", (byte) 0x00, 1, Unit.NONE),
      new DeviceMode(L_MOTOR, "L-MOTOR-ROT", (byte) 0x01, 1, Unit.NONE),
      new DeviceMode(L_MOTOR, "L-MOTOR-SPD", (byte) 0x02, 1, Unit.NONE)
    ),
    M_MOTOR, List.of(
      new DeviceMode(M_MOTOR, "M-MOTOR-DEG", (byte) 0x00, 1, Unit.NONE),
      new DeviceMode(M_MOTOR, "M-MOTOR-ROT", (byte) 0x01, 1, Unit.NONE),
      new DeviceMode(M_MOTOR, "M-MOTOR-SPD", (byte) 0x02, 1, Unit.NONE)
    ),
    EV3_COLOR, List.of(
      new DeviceMode(EV3_COLOR, "COL-REFLECT", (byte) 0x00, 1, Unit.PERCENT),
      new DeviceMode(EV3_COLOR, "COL-AMBIENT", (byte) 0x01, 1, Unit.PERCENT),
      new DeviceMode(EV3_COLOR, "COL-COLOR", (byte) 0x02, 1, Unit.COLOR),
      new DeviceMode(EV3_COLOR, "REF-RAW", (byte) 0x03, 2, Unit.NONE),
      new DeviceMode(EV3_COLOR, "RGB-RAW", (byte) 0x04, 3, Unit.NONE),
      new DeviceMode(EV3_COLOR, "COL-CAL", (byte) 0x05, 4, Unit.NONE)),
    EV3_TOUCH, List.of(new DeviceMode(EV3_TOUCH, "TOUCH", (byte) 0x00, 1, Unit.BUTTON)),
    EV3_ULTRASONIC, List.of(
      new DeviceMode(EV3_ULTRASONIC, "US-DIST-CM", (byte) 0x00, 1, Unit.SI),
      new DeviceMode(EV3_ULTRASONIC, "US-DIST-IN", (byte) 0x01, 1, Unit.SI),
      new DeviceMode(EV3_ULTRASONIC, "US-LISTEN", (byte) 0x02, 1, Unit.NONE),
      new DeviceMode(EV3_ULTRASONIC, "US-SI-CM", (byte) 0x03, 1, Unit.SI),
      new DeviceMode(EV3_ULTRASONIC, "US-SI-IN", (byte) 0x04, 1, Unit.SI),
      new DeviceMode(EV3_ULTRASONIC, "US-DC-CM", (byte) 0x05, 1, Unit.SI),
      new DeviceMode(EV3_ULTRASONIC, "US-DC-IN", (byte) 0x06, 1, Unit.SI)),
    EV3_GYRO, List.of(
      new DeviceMode(EV3_GYRO, "GYRO-ANG", (byte) 0x00, 1, Unit.SI),
      new DeviceMode(EV3_GYRO, "GYRO-RATE", (byte) 0x01, 1, Unit.SI),
      new DeviceMode(EV3_GYRO, "GYRO-FAS", (byte) 0x02, 1, Unit.NONE),
      new DeviceMode(EV3_GYRO, "GYRO-G&A", (byte) 0x03, 2, Unit.NONE),
      new DeviceMode(EV3_GYRO, "GYRO-CAL", (byte) 0x04, 4, Unit.NONE)),
    EV3_IR, List.of(
      new DeviceMode(EV3_IR, "IR-PROX", (byte) 0x00, 1, Unit.PERCENT),
      new DeviceMode(EV3_IR, "IR-SEEK", (byte) 0x01, 8, Unit.PERCENT),
      new DeviceMode(EV3_IR, "IR-REMOTE", (byte) 0x02, 4, Unit.BUTTON),
      new DeviceMode(EV3_IR, "IR-REM-A", (byte) 0x03, 1, Unit.NONE),
      new DeviceMode(EV3_IR, "IR-S-ALT", (byte) 0x05, 4, Unit.PERCENT),
      new DeviceMode(EV3_IR, "IR-CAL", (byte) 0x04, 2, Unit.NONE))
  );

  public static List<DeviceMode> get(DeviceType deviceType) {
    return DEVICE_MODES.get(deviceType);
  }

  public static Optional<DeviceMode> get(DeviceType deviceType, String modeName) {
    return DEVICE_MODES.get(deviceType).stream().filter(m -> m.name.equalsIgnoreCase(modeName)).findFirst();
  }

  public static Optional<DeviceMode> get(String deviceType, String modeName) {
    var type = DeviceType.fromName(deviceType);
    if (type.isEmpty())
      throw new NoSuchElementException("Unknown device type: " + deviceType);
    return get(type.get(), modeName);
  }

  public static Optional<DeviceMode> get(DeviceType deviceType, byte code) {
    return DEVICE_MODES.get(deviceType).stream().filter(m -> m.code == code).findFirst();
  }

  public static Optional<DeviceMode> get(String deviceType, byte code) {
    var type = DeviceType.fromName(deviceType);
    if (type.isEmpty())
      throw new NoSuchElementException("Unknown device type: " + deviceType);
    return get(type.get(), code);
  }

  public final DeviceType deviceType;
  public final int sampleSize;
  public final Unit unit;

  private DeviceMode(DeviceType deviceType, String modeName, byte modeCode, int sampleSize, Unit unit) {
    super(modeName, modeCode);
    this.deviceType = deviceType;
    this.sampleSize = sampleSize;
    this.unit = unit;
  }
}
