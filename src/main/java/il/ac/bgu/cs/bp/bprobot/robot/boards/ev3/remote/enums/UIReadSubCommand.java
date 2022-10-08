package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

public class UIReadSubCommand extends RemoteCode implements IRemoteAction {
  public static final UIReadSubCommand NONE = new UIReadSubCommand("NONE", (byte) 0x00);
  public static final UIReadSubCommand GET_VBATT = new UIReadSubCommand("GET_VBATT", (byte) 0x01);
  public static final UIReadSubCommand GET_IBATT = new UIReadSubCommand("GET_IBATT", (byte) 0x02);
  public static final UIReadSubCommand GET_OS_VERS = new UIReadSubCommand("GET_OS_VERS", (byte) 0x03);
  public static final UIReadSubCommand GET_EVENT = new UIReadSubCommand("GET_EVENT", (byte) 0x04);
  public static final UIReadSubCommand GET_TBATT = new UIReadSubCommand("GET_TBATT", (byte) 0x05);
  public static final UIReadSubCommand GET_IINT = new UIReadSubCommand("GET_IINT", (byte) 0x06);
  public static final UIReadSubCommand GET_IMOTOR = new UIReadSubCommand("GET_IMOTOR", (byte) 0x07);
  public static final UIReadSubCommand GET_STRING = new UIReadSubCommand("GET_STRING", (byte) 0x08);
  public static final UIReadSubCommand GET_HW_VERS = new UIReadSubCommand("GET_HW_VERS", (byte) 0x09);
  public static final UIReadSubCommand GET_FW_VERS = new UIReadSubCommand("GET_FW_VERS", (byte) 0x0A);
  public static final UIReadSubCommand GET_FW_BUILD = new UIReadSubCommand("GET_FW_BUILD", (byte) 0x0B);
  public static final UIReadSubCommand GET_OS_BUILD = new UIReadSubCommand("GET_OS_BUILD", (byte) 0x0C);
  public static final UIReadSubCommand GET_ADDRESS = new UIReadSubCommand("GET_ADDRESS", (byte) 0x0D);
  public static final UIReadSubCommand GET_CODE = new UIReadSubCommand("GET_CODE", (byte) 0x0E);
  public static final UIReadSubCommand KEY = new UIReadSubCommand("KEY", (byte) 0x0F);
  public static final UIReadSubCommand GET_SHUTDOWN = new UIReadSubCommand("GET_SHUTDOWN", (byte) 0x10);
  public static final UIReadSubCommand GET_WARNING = new UIReadSubCommand("GET_WARNING", (byte) 0x11);
  public static final UIReadSubCommand GET_LBATT = new UIReadSubCommand("GET_LBATT", (byte) 0x12);
  public static final UIReadSubCommand TEXTBOX_READ = new UIReadSubCommand("TEXTBOX_READ", (byte) 0x13);
  public static final UIReadSubCommand GET_VERSION = new UIReadSubCommand("GET_VERSION", (byte) 0x14);
  public static final UIReadSubCommand GET_IP = new UIReadSubCommand("GET_IP", (byte) 0x15);
  public static final UIReadSubCommand GET_POWER = new UIReadSubCommand("GET_POWER", (byte) 0x16);
  public static final UIReadSubCommand GET_SDCARD = new UIReadSubCommand("GET_SDCARD", (byte) 0x17);
  public static final UIReadSubCommand GET_USBSTICK = new UIReadSubCommand("GET_USBSTICK", (byte) 0x18);

  protected UIReadSubCommand(String name, byte code) {
    super(name, code);
  }
}
