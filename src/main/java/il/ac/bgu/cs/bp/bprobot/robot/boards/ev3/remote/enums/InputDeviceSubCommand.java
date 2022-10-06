package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.List;

public class InputDeviceSubCommand extends RemoteCode implements IRemoteAction {
  public static final InputDeviceSubCommand GET_FORMAT = new InputDeviceSubCommand("GET_FORMAT", (byte) 0x02);
  public static final InputDeviceSubCommand CAL_MINMAX = new InputDeviceSubCommand("CAL_MINMAX", (byte) 0x03);
  public static final InputDeviceSubCommand CAL_DEFAULT = new InputDeviceSubCommand("CAL_DEFAULT", (byte) 0x04);
  public static final InputDeviceSubCommand GET_TYPEMODE = new InputDeviceSubCommand("GET_TYPEMODE", (byte) 0x05);
  public static final InputDeviceSubCommand GET_SYMBOL = new InputDeviceSubCommand("GET_SYMBOL", (byte) 0x06);
  public static final InputDeviceSubCommand CAL_MIN = new InputDeviceSubCommand("CAL_MIN", (byte) 0x07);
  public static final InputDeviceSubCommand CAL_MAX = new InputDeviceSubCommand("CAL_MAX", (byte) 0x08);
  public static final InputDeviceSubCommand SETUP = new InputDeviceSubCommand("SETUP", (byte) 0x09);
  public static final InputDeviceSubCommand CLR_ALL = new InputDeviceSubCommand("CLR_ALL", (byte) 0x0A);
  public static final InputDeviceSubCommand GET_RAW = new InputDeviceSubCommand("GET_RAW", (byte) 0x0B);
  public static final InputDeviceSubCommand GET_CONNECTION = new InputDeviceSubCommand("GET_CONNECTION", (byte) 0x0C);
  public static final InputDeviceSubCommand STOP_ALL = new InputDeviceSubCommand("STOP_ALL", (byte) 0x0D);
  public static final InputDeviceSubCommand GET_NAME = new InputDeviceSubCommand("GET_NAME", (byte) 0x15);
  public static final InputDeviceSubCommand GET_MODENAME = new InputDeviceSubCommand("GET_MODENAME", (byte) 0x16);
  public static final InputDeviceSubCommand SET_RAW = new InputDeviceSubCommand("SET_RAW", (byte) 0x17);
  public static final InputDeviceSubCommand GET_FIGURES = new InputDeviceSubCommand("GET_FIGURES", (byte) 0x18);
  public static final InputDeviceSubCommand GET_CHANGES = new InputDeviceSubCommand("GET_CHANGES", (byte) 0x19);
  public static final InputDeviceSubCommand CLR_CHANGES = new InputDeviceSubCommand("CLR_CHANGES", (byte) 0x1A);
  public static final InputDeviceSubCommand READY_PCT = new InputDeviceSubCommand("READY_PCT", (byte) 0x1B);
  public static final InputDeviceSubCommand READY_RAW = new InputDeviceSubCommand("READY_RAW", (byte) 0x1C);
  public static final InputDeviceSubCommand READY_SI = new InputDeviceSubCommand("READY_SI", (byte) 0x1D);
  public static final InputDeviceSubCommand GET_MINMAX = new InputDeviceSubCommand("GET_MINMAX", (byte) 0x1E);
  public static final InputDeviceSubCommand GET_BUMPS = new InputDeviceSubCommand("GET_BUMPS", (byte) 0x1F);

  public static final List<InputDeviceSubCommand> values = List.of(
      GET_FORMAT,
      CAL_MINMAX,
      CAL_DEFAULT,
      GET_TYPEMODE,
      GET_SYMBOL,
      CAL_MIN,
      CAL_MAX,
      SETUP,
      CLR_ALL,
      GET_RAW,
      GET_CONNECTION,
      STOP_ALL,
      GET_NAME,
      GET_MODENAME,
      SET_RAW,
      GET_FIGURES,
      GET_CHANGES,
      CLR_CHANGES,
      READY_PCT,
      READY_RAW,
      READY_SI,
      GET_MINMAX,
      GET_BUMPS
  );

  protected InputDeviceSubCommand(String name, byte code) {
    super(name, code);
  }

  public static InputDeviceSubCommand fromCode(byte code) {
    return values.stream().filter(v -> v.code == code).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown InputDeviceSubCommand code: " + code));
  }
}
