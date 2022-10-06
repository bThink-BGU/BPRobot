package il.ac.bgu.cs.bp.bprobot.remote.enums;

import il.ac.bgu.cs.bp.bprobot.remote.model.com.ICommunicator;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.ByteCodeFormatter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import static il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.Ev3Constants.*;
import static il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.Ev3Constants.LAYER_MASTER;

public abstract class Input extends RemoteCode {
  public static final Input SAMPLE = new Input("SAMPLE", (byte) 0x97) {
  };
  public static final Input DEVICE_LIST = new Input("DEVICE_LIST", (byte) 0x98) {
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

  private static final List<Input> values = List.of(
      SAMPLE,DEVICE_LIST,DEVICE,READ,TEST,READY,READSI,READEXT,WRITE
  );

  public static Input fromCode(byte code) {
    return values.stream().filter(v -> v.code == code).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown Input code: " + code));
  }

  private Input(String name, byte code) {
    super(name, code);
  }
}
