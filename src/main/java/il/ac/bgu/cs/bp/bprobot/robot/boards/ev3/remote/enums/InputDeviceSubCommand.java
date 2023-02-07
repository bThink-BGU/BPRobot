package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

public class InputDeviceSubCommand extends RemoteCode implements IRemoteAction {
  public static final InputDeviceSubCommand NONE = new InputDeviceSubCommand("NONE", (byte) 0x00);
  public static final InputDeviceSubCommand GET_FORMAT = new InputDeviceSubCommand("GET-FORMAT", (byte) 0x02);
  public static final InputDeviceSubCommand CAL_MINMAX = new InputDeviceSubCommand("CAL-MINMAX", (byte) 0x03);
  public static final InputDeviceSubCommand CAL_DEFAULT = new InputDeviceSubCommand("CAL-DEFAULT", (byte) 0x04);
  public static final InputDeviceSubCommand GET_TYPEMODE = new InputDeviceSubCommand("GET-TYPEMODE", (byte) 0x05);
  public static final InputDeviceSubCommand GET_SYMBOL = new InputDeviceSubCommand("GET-SYMBOL", (byte) 0x06);
  public static final InputDeviceSubCommand CAL_MIN = new InputDeviceSubCommand("CAL-MIN", (byte) 0x07);
  public static final InputDeviceSubCommand CAL_MAX = new InputDeviceSubCommand("CAL-MAX", (byte) 0x08);
  public static final InputDeviceSubCommand SETUP = new InputDeviceSubCommand("SETUP", (byte) 0x09);
  public static final InputDeviceSubCommand CLR_ALL = new InputDeviceSubCommand("CLR-ALL", (byte) 0x0A);
  public static final InputDeviceSubCommand GET_RAW = new InputDeviceSubCommand("GET-RAW", (byte) 0x0B);
  public static final InputDeviceSubCommand GET_CONNECTION = new InputDeviceSubCommand("GET-CONNECTION", (byte) 0x0C);
  public static final InputDeviceSubCommand STOP_ALL = new InputDeviceSubCommand("STOP-ALL", (byte) 0x0D);
  public static final InputDeviceSubCommand GET_NAME = new InputDeviceSubCommand("GET-NAME", (byte) 0x15);
  public static final InputDeviceSubCommand GET_MODENAME = new InputDeviceSubCommand("GET-MODENAME", (byte) 0x16);
  public static final InputDeviceSubCommand SET_RAW = new InputDeviceSubCommand("SET-RAW", (byte) 0x17);
  public static final InputDeviceSubCommand GET_FIGURES = new InputDeviceSubCommand("GET-FIGURES", (byte) 0x18);
  public static final InputDeviceSubCommand GET_CHANGES = new InputDeviceSubCommand("GET-CHANGES", (byte) 0x19);
  public static final InputDeviceSubCommand CLR_CHANGES = new InputDeviceSubCommand("CLR-CHANGES", (byte) 0x1A);
  public static final InputDeviceSubCommand READY_PCT = new InputDeviceSubCommand("READY-PCT", (byte) 0x1B);
  public static final InputDeviceSubCommand READY_RAW = new InputDeviceSubCommand("READY-RAW", (byte) 0x1C);
  public static final InputDeviceSubCommand READY_SI = new InputDeviceSubCommand("READY-SI", (byte) 0x1D);
  public static final InputDeviceSubCommand GET_MINMAX = new InputDeviceSubCommand("GET-MINMAX", (byte) 0x1E);
  public static final InputDeviceSubCommand GET_BUMPS = new InputDeviceSubCommand("GET-BUMPS", (byte) 0x1F);

  public static final Map<String, InputDeviceSubCommand> INPUT_DEVICE_SUB_COMMANDS = Map.ofEntries(
    entry("GET-FORMAT", GET_FORMAT),
    entry("CAL-MINMAX", CAL_MINMAX),
    entry("CAL-DEFAULT", CAL_DEFAULT),
    entry("GET-TYPEMODE", GET_TYPEMODE),
    entry("GET-SYMBOL", GET_SYMBOL),
    entry("CAL-MIN", CAL_MIN),
    entry("CAL-MAX", CAL_MAX),
    entry("SETUP", SETUP),
    entry("CLR-ALL", CLR_ALL),
    entry("GET-RAW", GET_RAW),
    entry("GET-CONNECTION", GET_CONNECTION),
    entry("STOP-ALL", STOP_ALL),
    entry("GET-NAME", GET_NAME),
    entry("GET-MODENAME", GET_MODENAME),
    entry("SET-RAW", SET_RAW),
    entry("GET-FIGURES", GET_FIGURES),
    entry("GET-CHANGES", GET_CHANGES),
    entry("CLR-CHANGES", CLR_CHANGES),
    entry("READY-PCT", READY_PCT),
    entry("READY-RAW", READY_RAW),
    entry("READY-SI", READY_SI),
    entry("GET-MINMAX", GET_MINMAX),
    entry("GET-BUMPS", GET_BUMPS)
  );

  protected InputDeviceSubCommand(String name, byte code) {
    super(name, code);
  }

  public static Optional<InputDeviceSubCommand> fromCode(byte code) {
    return INPUT_DEVICE_SUB_COMMANDS.values().stream().filter(v -> v.code == code).findFirst();
  }

  public static Optional<InputDeviceSubCommand> fromName(String name) {
    return Optional.ofNullable(INPUT_DEVICE_SUB_COMMANDS.get(name.toUpperCase()));
  }
}
