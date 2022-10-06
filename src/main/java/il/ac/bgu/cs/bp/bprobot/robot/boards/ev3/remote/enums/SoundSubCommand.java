package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

public class SoundSubCommand extends RemoteCode {
  public static final SoundSubCommand BREAK = new SoundSubCommand("BREAK", (byte) 0x00);
  public static final SoundSubCommand TONE = new SoundSubCommand("TONE", (byte) 0x01);
  public static final SoundSubCommand PLAY = new SoundSubCommand("PLAY", (byte) 0x02);
  public static final SoundSubCommand REPEAT = new SoundSubCommand("REPEAT", (byte) 0x03);
  public static final SoundSubCommand SERVICE = new SoundSubCommand("SERVICE", (byte) 0x04);

  protected SoundSubCommand(String name, byte code) {
    super(name, code);
  }
}
