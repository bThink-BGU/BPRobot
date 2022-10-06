package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

public class Port extends RemoteCode {
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
}
