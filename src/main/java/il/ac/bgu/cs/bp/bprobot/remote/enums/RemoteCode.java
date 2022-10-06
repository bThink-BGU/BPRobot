package il.ac.bgu.cs.bp.bprobot.remote.enums;

public abstract class RemoteCode {
  public final String name;
  public final byte code;

  protected RemoteCode(String name, byte code) {
    this.name = name;
    this.code = code;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + name;
  }
}
