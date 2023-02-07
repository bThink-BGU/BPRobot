package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Input extends RemoteCode {
  public static final Input SAMPLE = new Input("SAMPLE", (byte) 0x97) {
  };
  public static final Input DEVICE_LIST = new Input("DEVICE-LIST", (byte) 0x98) {
  };
  public static final Input DEVICE = new Input("DEVICE", (byte) 0x99) {
  };
  public static final Input READ = new Input("READ", (byte) 0x9A) {
  };
  public static final Input TEST = new Input("TEST", (byte) 0x9B) {
  };
  public static final Input READY = new Input("READY", (byte) 0x9C) {
  };
  public static final Input READSI = new Input("READSI", (byte) 0x9D) {

  };
  public static final Input READEXT = new Input("READEXT", (byte) 0x9E) {
  };
  public static final Input WRITE = new Input("WRITE", (byte) 0x9F) {
  };

  public static final Map<String, Input> INPUTS = Map.of(
    "SAMPLE", SAMPLE,
    "DEVICE_LIST", DEVICE_LIST,
    "DEVICE", DEVICE,
    "READ", READ,
    "TEST", TEST,
    "READY", READY,
    "READSI", READSI,
    "READEXT", READEXT,
    "WRITE", WRITE
  );

  public static Optional<Input> fromName(String name) {
    return Optional.ofNullable(INPUTS.get(name.toUpperCase()));
  }

  public static Optional<Input> fromCode(byte code) {
    return INPUTS.values().stream().filter(t->t.code == code).findFirst();
  }

  private Input(String name, byte code) {
    super(name, code);
  }
}
