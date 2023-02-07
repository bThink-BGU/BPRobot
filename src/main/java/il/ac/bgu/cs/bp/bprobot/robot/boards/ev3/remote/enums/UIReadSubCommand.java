package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

public class UIReadSubCommand extends RemoteCode implements IRemoteAction {
  public static final UIReadSubCommand NONE = new UIReadSubCommand("NONE", (byte) 0x00);
  public static final UIReadSubCommand GET_VBATT = new UIReadSubCommand("GET-VBATT", (byte) 0x01);
  public static final UIReadSubCommand GET_IBATT = new UIReadSubCommand("GET-IBATT", (byte) 0x02);
  public static final UIReadSubCommand GET_OS_VERS = new UIReadSubCommand("GET-OS-VERS", (byte) 0x03);
  public static final UIReadSubCommand GET_EVENT = new UIReadSubCommand("GET-EVENT", (byte) 0x04);
  public static final UIReadSubCommand GET_TBATT = new UIReadSubCommand("GET-TBATT", (byte) 0x05);
  public static final UIReadSubCommand GET_IINT = new UIReadSubCommand("GET-IINT", (byte) 0x06);
  public static final UIReadSubCommand GET_IMOTOR = new UIReadSubCommand("GET-IMOTOR", (byte) 0x07);
  public static final UIReadSubCommand GET_STRING = new UIReadSubCommand("GET-STRING", (byte) 0x08);
  public static final UIReadSubCommand GET_HW_VERS = new UIReadSubCommand("GET-HW-VERS", (byte) 0x09);
  public static final UIReadSubCommand GET_FW_VERS = new UIReadSubCommand("GET-FW-VERS", (byte) 0x0A);
  public static final UIReadSubCommand GET_FW_BUILD = new UIReadSubCommand("GET-FW-BUILD", (byte) 0x0B);
  public static final UIReadSubCommand GET_OS_BUILD = new UIReadSubCommand("GET-OS-BUILD", (byte) 0x0C);
  public static final UIReadSubCommand GET_ADDRESS = new UIReadSubCommand("GET-ADDRESS", (byte) 0x0D);
  public static final UIReadSubCommand GET_CODE = new UIReadSubCommand("GET-CODE", (byte) 0x0E);
  public static final UIReadSubCommand KEY = new UIReadSubCommand("KEY", (byte) 0x0F);
  public static final UIReadSubCommand GET_SHUTDOWN = new UIReadSubCommand("GET-SHUTDOWN", (byte) 0x10);
  public static final UIReadSubCommand GET_WARNING = new UIReadSubCommand("GET-WARNING", (byte) 0x11);
  public static final UIReadSubCommand GET_LBATT = new UIReadSubCommand("GET-LBATT", (byte) 0x12);
  public static final UIReadSubCommand TEXTBOX_READ = new UIReadSubCommand("TEXTBOX-READ", (byte) 0x13);
  public static final UIReadSubCommand GET_VERSION = new UIReadSubCommand("GET-VERSION", (byte) 0x14);
  public static final UIReadSubCommand GET_IP = new UIReadSubCommand("GET-IP", (byte) 0x15);
  public static final UIReadSubCommand GET_POWER = new UIReadSubCommand("GET-POWER", (byte) 0x16);
  public static final UIReadSubCommand GET_SDCARD = new UIReadSubCommand("GET-SDCARD", (byte) 0x17);
  public static final UIReadSubCommand GET_USBSTICK = new UIReadSubCommand("GET-USBSTICK", (byte) 0x18);

  protected UIReadSubCommand(String name, byte code) {
    super(name, code);
  }

  public static final Map<String, UIReadSubCommand> UI_READ_SUB_COMMAND = Map.ofEntries(
    entry("NONE", NONE),
    entry("GET-VBATT", GET_VBATT),
    entry("GET-IBATT", GET_IBATT),
    entry("GET-OS-VERS", GET_OS_VERS),
    entry("GET-EVENT", GET_EVENT),
    entry("GET-TBATT", GET_TBATT),
    entry("GET-IINT", GET_IINT),
    entry("GET-IMOTOR", GET_IMOTOR),
    entry("GET-STRING", GET_STRING),
    entry("GET-HW-VERS", GET_HW_VERS),
    entry("GET-FW-VERS", GET_FW_VERS),
    entry("GET-FW-BUILD", GET_FW_BUILD),
    entry("GET-OS-BUILD", GET_OS_BUILD),
    entry("GET-ADDRESS", GET_ADDRESS),
    entry("GET-CODE", GET_CODE),
    entry("KEY", KEY),
    entry("GET-SHUTDOWN", GET_SHUTDOWN),
    entry("GET-WARNING", GET_WARNING),
    entry("GET-LBATT", GET_LBATT),
    entry("TEXTBOX-READ", TEXTBOX_READ),
    entry("GET-VERSION", GET_VERSION),
    entry("GET-IP", GET_IP),
    entry("GET-POWER", GET_POWER),
    entry("GET-SDCARD", GET_SDCARD),
    entry("GET-USBSTICK", GET_USBSTICK)
  );

  public static Optional<UIReadSubCommand> fromName(String name) {
    return Optional.ofNullable(UI_READ_SUB_COMMAND.get(name.toUpperCase()));
  }

  public static Optional<UIReadSubCommand> fromCode(byte code) {
    return UI_READ_SUB_COMMAND.values().stream().filter(c -> c.code == code).findFirst();
  }
}
