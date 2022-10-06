package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

public class Layer extends RemoteCode {
  public static final Layer MASTER = new Layer("MASTER", (byte) 0x00);
  public static final Layer SLAVE = new Layer("SLAVE", (byte) 0x01);
  protected Layer(String name, byte code) {
    super(name, code);
  }
}
