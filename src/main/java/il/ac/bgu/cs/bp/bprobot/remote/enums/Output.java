package il.ac.bgu.cs.bp.bprobot.remote.enums;

import java.util.List;

public class Output extends RemoteCode {
  public static final Output GET_TYPE = new Output("GET_TYPE", (byte) 0xA0);
  public static final Output SET_MODE = new Output("SET_MODE", (byte) 0xA1);
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
  public static final Output STEP_POWER = new Output("STEP_POWER", (byte) 0xAC);
  public static final Output TIME_POWER = new Output("TIME_POWER", (byte) 0xAD);
  public static final Output STEP_SPEED = new Output("STEP_SPEED", (byte) 0xAE);
  public static final Output TIME_SPEED = new Output("TIME_SPEED", (byte) 0xAF);
  public static final Output STEP_SYNC = new Output("STEP_SYNC", (byte) 0xB0);
  public static final Output TIME_SYNC = new Output("TIME_SYNC", (byte) 0xB1);
  public static final Output CLR_COUNT = new Output("CLR_COUNT", (byte) 0xB2);
  public static final Output GET_COUNT = new Output("GET_COUNT", (byte) 0xB3);
  public static final Output PRG_STOP = new Output("PRG_STOP", (byte) 0xB4);

  public static final List<Output> values = List.of(
      GET_TYPE, SET_MODE, RESET, STOP, POWER, SPEED, START, POLARITY, READ, TEST, READY, POSITION, STEP_POWER, TIME_POWER, STEP_SPEED, TIME_SPEED, STEP_SYNC, TIME_SYNC, CLR_COUNT, GET_COUNT, PRG_STOP
  );

  protected Output(String name, byte code) {
    super(name, code);
  }
}
