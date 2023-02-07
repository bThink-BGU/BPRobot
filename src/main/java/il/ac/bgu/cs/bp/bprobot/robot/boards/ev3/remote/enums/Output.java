package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

public class Output extends RemoteCode implements IRemoteAction {
  public static final Output GET_TYPE = new Output("GET-TYPE", (byte) 0xA0);
  public static final Output SET_MODE = new Output("SET-MODE", (byte) 0xA1);
  public static final Output RESET = new Output("RESET", (byte) 0xA2);
  public static final Output STOP = new Output("STOP", (byte) 0xA3);
  public static final Output POWER = new Output("POWER", (byte) 0xA4);
  public static final Output SPEED = new Output("SPEED", (byte) 0xA5);
  public static final Output START = new Output("START", (byte) 0xA6);
  public static final Output POLARITY = new Output("POLARITY", (byte) 0xA7);
  public static final Output READ = new Output("READ", (byte) 0xA8);
  public static final Output TEST = new Output("TEST", (byte) 0xA9);
  public static final Output READY = new Output("READY", (byte) 0xAA);
  public static final Output POSITION = new Output("POS", (byte) 0xAB);
  public static final Output STEP_POWER = new Output("STEP-POWER", (byte) 0xAC);
  public static final Output TIME_POWER = new Output("TIME-POWER", (byte) 0xAD);
  public static final Output STEP_SPEED = new Output("STEP-SPEED", (byte) 0xAE);
  public static final Output TIME_SPEED = new Output("TIME-SPEED", (byte) 0xAF);
  public static final Output STEP_SYNC = new Output("STEP-SYNC", (byte) 0xB0);
  public static final Output TIME_SYNC = new Output("TIME-SYNC", (byte) 0xB1);
  public static final Output CLR_COUNT = new Output("CLR-COUNT", (byte) 0xB2);
  public static final Output GET_COUNT = new Output("GET-COUNT", (byte) 0xB3);
  public static final Output PRG_STOP = new Output("PRG-STOP", (byte) 0xB4);

  public static final Map<String, Output> OUTPUTS = Map.ofEntries(
    entry("GET-TYPE", GET_TYPE),
    entry("SET-MODE", SET_MODE),
    entry("RESET", RESET),
    entry("STOP", STOP),
    entry("POWER", POWER),
    entry("SPEED", SPEED),
    entry("START", START),
    entry("POLARITY", POLARITY),
    entry("READ", READ),
    entry("TEST", TEST),
    entry("READY", READY),
    entry("POSITION", POSITION),
    entry("STEP-POWER", STEP_POWER),
    entry("TIME-POWER", TIME_POWER),
    entry("STEP-SPEED", STEP_SPEED),
    entry("TIME-SPEED", TIME_SPEED),
    entry("STEP-SYNC", STEP_SYNC),
    entry("TIME-SYNC", TIME_SYNC),
    entry("CLR-COUNT", CLR_COUNT),
    entry("GET-COUNT", GET_COUNT),
    entry("PRG-STOP", PRG_STOP)
  );

  public static Optional<Output> fromName(String name) {
    return Optional.ofNullable(OUTPUTS.get(name.toUpperCase()));
  }

  public static Optional<Output> fromCode(byte code) {
    return OUTPUTS.values().stream().filter(t->t.code == code).findFirst();
  }

  protected Output(String name, byte code) {
    super(name, code);
  }
}
