package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

public class UI extends RemoteCode implements IRemoteAction {
  public static final UI FLUSH = new UI("FLUSH", (byte)0x80);
  public static final UI READ = new UI("READ", (byte)0x81);
  public static final UI WRITE = new UI("WRITE", (byte)0x82);
  public static final UI BUTTON = new UI("BUTTON", (byte)0x83);
  public static final UI DRAW = new UI("DRAW", (byte)0x84);

  protected UI(String name, byte code) {
    super(name, code);
  }
}
