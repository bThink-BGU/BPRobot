package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

public class SoundSubCommand extends RemoteCode implements IRemoteAction{
  public static final SoundSubCommand BREAK = new SoundSubCommand("BREAK", (byte) 0x00);
  public static final SoundSubCommand TONE = new SoundSubCommand("TONE", (byte) 0x01);
  public static final SoundSubCommand PLAY = new SoundSubCommand("PLAY", (byte) 0x02);
  public static final SoundSubCommand REPEAT = new SoundSubCommand("REPEAT", (byte) 0x03);
  public static final SoundSubCommand SERVICE = new SoundSubCommand("SERVICE", (byte) 0x04);

  protected SoundSubCommand(String name, byte code) {
    super(name, code);
  }

  public static final Map<String, SoundSubCommand> SOUND_SUB_COMMANDS = Map.of(
    "BREAK", BREAK,
    "TONE", TONE,
    "PLAY", PLAY,
    "REPEAT", REPEAT,
    "SERVICE", SERVICE
  );

  public static Optional<SoundSubCommand> fromName(String name) {
    return Optional.ofNullable(SOUND_SUB_COMMANDS.get(name.toUpperCase()));
  }

  public static Optional<SoundSubCommand> fromCode(byte code) {
    return SOUND_SUB_COMMANDS.values().stream().filter(t->t.code == code).findFirst();
  }
}
