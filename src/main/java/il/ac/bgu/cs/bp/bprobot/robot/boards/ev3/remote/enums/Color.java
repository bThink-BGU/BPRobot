package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

public class Color extends RemoteCode {
  public static final Color NONE = new Color("NONE", (byte) 0x00);
  public static final Color BLACK = new Color("BLACK", (byte) 0x01);
  public static final Color BLUE = new Color("BLUE", (byte) 0x02);
  public static final Color GREEN = new Color("GREEN", (byte) 0x03);
  public static final Color YELLOW = new Color("YELLOW", (byte) 0x04);
  public static final Color RED = new Color("RED", (byte) 0x05);
  public static final Color WHITE = new Color("WHITE", (byte) 0x06);
  public static final Color BROWN = new Color("BROWN", (byte) 0x07);

  protected Color(String name, byte code) {
    super(name, code);
  }

  public static final Map<String, Color> COLORS = Map.of(
    "NONE", NONE,
    "BLACK", BLACK,
    "BLUE", BLUE,
    "GREEN", GREEN,
    "YELLOW", YELLOW,
    "RED", RED,
    "WHITE", WHITE,
    "BROWN", BROWN);

  public static Optional<Color> fromName(String name) {
    return Optional.ofNullable(COLORS.get(name.toUpperCase()));
  }

  public static Optional<Color> fromCode(byte code) {
    return COLORS.values().stream().filter(t->t.code == code).findFirst();
  }
}
