package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

public class Unit extends RemoteCode {
  public static final Unit NONE = new Unit("NONE", (byte) 0x00, InputDeviceSubCommand.NONE);
  public static final Unit SI = new Unit("SI", (byte) 0x01, InputDeviceSubCommand.READY_SI);
  public static final Unit PERCENT = new Unit("PERCENT", (byte) 0x02, InputDeviceSubCommand.READY_PCT);
  public static final Unit COLOR = new Unit("COLOR", (byte) 0x03, InputDeviceSubCommand.READY_SI);
  public static final Unit BUTTON = new Unit("BUTTON", (byte) 0x04, InputDeviceSubCommand.READY_SI);
  public final InputDeviceSubCommand command;

  protected Unit(String name, byte code, InputDeviceSubCommand command) {
    super(name, code);
    this.command = command;
  }

  public static final Map<String, Unit> UNITS = Map.of(
    "NONE", NONE,
    "SI", SI,
    "PERCENT", PERCENT,
    "COLOR", COLOR,
    "BUTTON", BUTTON
  );

  public static Optional<Unit> fromName(String name) {
    return Optional.ofNullable(UNITS.get(name.toUpperCase()));
  }

  public static Optional<Unit> fromCode(byte code) {
    return UNITS.values().stream().filter(t->t.code == code).findFirst();
  }
}
