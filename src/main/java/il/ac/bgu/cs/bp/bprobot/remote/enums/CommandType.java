package il.ac.bgu.cs.bp.bprobot.remote.enums;

import java.util.List;

public class CommandType extends RemoteCode {
  // Command Types
  public static final CommandType DIRECT_COMMAND_REPLY = new CommandType("DIRECT_COMMAND_REPLY", (byte) 0x00);
  public static final CommandType DIRECT_COMMAND_NOREPLY = new CommandType("DIRECT_COMMAND_NOREPLY", (byte) 0x80);
  public static final CommandType DIRECT_COMMAND_SUCCESS = new CommandType("DIRECT_COMMAND_SUCCESS", (byte) 0x02);
  public static final CommandType DIRECT_COMMAND_FAIL = new CommandType("DIRECT_COMMAND_FAIL", (byte) 0x04);

  public static List<CommandType> values = List.of(
      DIRECT_COMMAND_REPLY, DIRECT_COMMAND_NOREPLY, DIRECT_COMMAND_SUCCESS, DIRECT_COMMAND_FAIL
  );

  protected CommandType(String name, byte code) {
    super(name, code);
  }
}
