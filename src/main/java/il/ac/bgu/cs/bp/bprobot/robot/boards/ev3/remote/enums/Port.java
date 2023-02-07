package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.Map;
import java.util.Optional;

public class Port extends RemoteCode implements lejos.hardware.port.Port {
  public static final Port A = new Port("A", false, (byte) 0x00);
  public static final Port B = new Port("B", false, (byte) 0x01);
  public static final Port C = new Port("C", false, (byte) 0x02);
  public static final Port D = new Port("D", false, (byte) 0x03);
  public static final Port S1 = new Port("S1", true, (byte) 0x00);
  public static final Port S2 = new Port("S2", true, (byte) 0x01);
  public static final Port S3 = new Port("S3", true, (byte) 0x02);
  public static final Port S4 = new Port("S4", true, (byte) 0x03);
  public final boolean isSensor;

  protected Port(String name, boolean isSensor, byte code) {
    super(name, code);
    this.isSensor = isSensor;
  }

  @Override
  public String getName() {
    return name;
  }

  public static final Map<String, Port> PORTS = Map.of(
    "A",A,
    "B",B,
    "C",C,
    "D",D,
    "S1",S1,
    "S2",S2,
    "S3",S3,
    "S4",S4
  );

  public static Optional<Port> fromName(String name) {
    return Optional.ofNullable(PORTS.get(name.toUpperCase()));
  }

  public static Optional<Port> fromCode(byte code) {
    return PORTS.values().stream().filter(t->t.code == code).findFirst();
  }
}
