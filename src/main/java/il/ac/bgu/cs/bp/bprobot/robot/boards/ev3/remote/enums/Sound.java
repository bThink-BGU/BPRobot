package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

public class Sound extends RemoteCode implements IRemoteAction {
  public static final Sound SOUND = new Sound("SOUND", (byte) 0x94);
  public static final Sound TEST = new Sound("TEST", (byte) 0x95);
  public static final Sound READY = new Sound("READY", (byte) 0x96);

  protected Sound(String name, byte code) {
    super(name, code);
  }

  public static final Map<String, Sound> SOUNDS = Map.of(
    "SOUND", SOUND,
    "TEST", TEST,
    "READY", READY
  );

  public static Optional<Sound> fromName(String name) {
    return Optional.ofNullable(SOUNDS.get(name.toUpperCase()));
  }

  public static Optional<Sound> fromCode(byte code) {
    return SOUNDS.values().stream().filter(t->t.code == code).findFirst();
  }
}
