package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

public class Sound extends RemoteCode {
  public static final Sound SOUND = new Sound("SOUND", (byte) 0x94);
  public static final Sound TEST = new Sound("TEST", (byte) 0x95);
  public static final Sound READY = new Sound("READY", (byte) 0x96);

  protected Sound(String name, byte code) {
    super(name, code);
  }
}
