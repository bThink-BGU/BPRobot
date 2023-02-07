package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandType extends RemoteCode {
  // Command Types
  public static final CommandType DIRECT_COMMAND_REPLY = new CommandType("DIRECT_COMMAND_REPLY", (byte) 0x00);
  public static final CommandType DIRECT_COMMAND_NOREPLY = new CommandType("DIRECT_COMMAND_NOREPLY", (byte) 0x80);
  public static final CommandType DIRECT_COMMAND_SUCCESS = new CommandType("DIRECT_COMMAND_SUCCESS", (byte) 0x02);
  public static final CommandType DIRECT_COMMAND_FAIL = new CommandType("DIRECT_COMMAND_FAIL", (byte) 0x04);

  public static final Map<String, CommandType> COMMAND_TYPES = Map.of(
    "DIRECT_COMMAND_REPLY", DIRECT_COMMAND_REPLY,
    "DIRECT_COMMAND_NOREPLY", DIRECT_COMMAND_NOREPLY,
    "DIRECT_COMMAND_SUCCESS", DIRECT_COMMAND_SUCCESS,
    "DIRECT_COMMAND_FAIL", DIRECT_COMMAND_FAIL
  );

  public static Optional<CommandType> fromName(String name) {
    return Optional.ofNullable(COMMAND_TYPES.get(name.toUpperCase()));
  }

  public static Optional<CommandType> fromCode(byte code) {
    return COMMAND_TYPES.values().stream().filter(t->t.code == code).findFirst();
  }

  protected CommandType(String name, byte code) {
    super(name, code);
  }
}
