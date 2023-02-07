package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

public class Layer extends RemoteCode {
  public static final Layer MASTER = new Layer("MASTER", (byte) 0x00);
  public static final Layer SLAVE = new Layer("SLAVE", (byte) 0x01);
  protected Layer(String name, byte code) {
    super(name, code);
  }

  public static final Map<String, Layer> LAYERS = Map.of(
    "MASTER", MASTER,
    "SLAVE", SLAVE
  );

  public static Optional<Layer> fromName(String name) {
    return Optional.ofNullable(LAYERS.get(name));
  }

  public static Optional<Layer> fromCode(byte code) {
    return LAYERS.values().stream().filter(l -> l.code == code).findFirst();
  }
}
