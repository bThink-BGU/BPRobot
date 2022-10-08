/**
 * Copyright (C) 2011-2017 The PILE Developers <pile-dev@googlegroups.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol;

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.ByteCodeFormatter;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.*;
import il.ac.bgu.cs.bp.bprobot.util.Log;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.communication.ICommunicator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;

/**
 * A protocol class for LEGO MINDSTORMS EV3.
 */
public class Ev3Protocol extends ProtocolBase {
  public static final String KEY_VALUE = "value";
  private static final String TAG = "Ev3Protocol";
  private static final byte OUTPUT_PORT_OFFSET = 0x10;

  public Ev3Protocol(ICommunicator comm) {
    super(comm);
  }

  @Override
  public void open() throws IOException {
    mCommunicator.open();
  }

  @Override
  public void close() {
    mCommunicator.close();
  }

  @Override
  public Map<String, Object> exec(IRemoteAction action, Map<String, Object> args) {
    if (InputDeviceSubCommand.READY_SI == action) {
      var port = (Port) args.get("port");
      if (port == null) throw new IllegalArgumentException("port is null");
      var type = (DeviceType) args.getOrDefault("type", DeviceType.DONT_CHANGE);
      var mode = (DeviceMode) args.getOrDefault("mode", DeviceMode.DONT_CHANGE);
      return Map.of(KEY_VALUE, getSiValue(port, type, mode, (int) args.get("nvalue")));
    }
    if (InputDeviceSubCommand.READY_PCT == action) {
      var port = (Port) args.get("port");
      if (port == null) throw new IllegalArgumentException("port is null");
      var type = (DeviceType) args.getOrDefault("type", DeviceType.DONT_CHANGE);
      var mode = (DeviceMode) args.getOrDefault("mode", DeviceMode.DONT_CHANGE);
      return Map.of(KEY_VALUE, getPercentValue(port, type, mode, (int) args.get("nvalue")));
    }
    if (UIReadSubCommand.GET_VBATT == action) {
      return Map.of(KEY_VALUE, getBatteryVoltage());
    }
    if (Output.RESET == action) {
      var port = (Port) args.get("port");
      if (port == null) throw new IllegalArgumentException("port is null");
      resetTachoCounts(port);
      return Map.of();
    }
    if (Output.SPEED == action) {
      var port = (Port) args.get("port");
      if (port == null) throw new IllegalArgumentException("port is null");
      var speed = (Integer) args.get("speed");
      if (speed == null) throw new IllegalArgumentException("speed not defined");
      setOutputSpeed(port, speed);
      return Map.of();
    }
    if (Output.POWER == action) {
      var port = (Port) args.get("port");
      if (port == null) throw new IllegalArgumentException("port is null");
      var power = (Integer) args.get("power");
      if (power == null) throw new IllegalArgumentException("power not defined");
      setOutputPower(port, power);
      return Map.of();
    }
    if (Output.TEST == action) {
      var port = (Port) args.get("port");
      if (port == null) throw new IllegalArgumentException("port is null");
      return Map.of(KEY_VALUE, testOutput(port));
    }
    if (Output.STEP_POWER == action) {
      var port = (Port) args.get("port");
      if (port == null) throw new IllegalArgumentException("port is null");
      var power = (Integer) args.get("power");
      if (power == null) throw new IllegalArgumentException("power not defined");
      var steps = (int[]) args.get("steps");
      if (steps == null) throw new IllegalArgumentException("steps not defined");
      var brake = (int) args.getOrDefault("brake", 1);
      stepPower(port, power, steps, brake);
      return Map.of();
    }
    throw new UnsupportedOperationException("Unsupported command: " + action);
  }

  /**
   * Gets the value in SI unit from a machine. This method sends a request and receives the result.
   *
   * @param port   the port of a device
   * @param type   the device type
   * @param mode   the mode of the device
   * @param nvalue the number of the response value
   * @return a returned value in SI unit
   */
  private float[] getSiValue(Port port, DeviceType type, DeviceMode mode, int nvalue) {
    byte[] command = inputDeviceCommand(InputDeviceSubCommand.READY_SI, port, type, mode, nvalue, 4);
    mCommunicator.write(command);

    byte[] reply = readAnswer();

    // check the validity of the response
    boolean valid = (reply[2] == CommandType.DIRECT_COMMAND_SUCCESS.code);

    // read the SI unit value in float type
    float[] result = new float[nvalue];
    for (int i = 0; i < nvalue; i++) {
      byte[] data = Arrays.copyOfRange(reply, 3 + 4 * i, 7 + 4 * i);
      result[i] = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }
    return result;
  }


  private float getBatteryVoltage() {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();
    byteCode.addOpCode(CommandType.DIRECT_COMMAND_REPLY);

    // TODO: NOT TESTED when nvalue is more than 2
    byteCode.addGlobalAndLocalBufferSize(4, 0);
    byteCode.addOpCode(UIReadSubCommand.GET_VBATT);
    byteCode.addGlobalIndex((byte) 0x00);
    mCommunicator.write(byteCode.byteArray());

    byte[] reply = readAnswer();

    // check the validity of the response
    boolean valid = (reply[2] == CommandType.DIRECT_COMMAND_SUCCESS.code);

    byte[] result = new byte[]{reply[3]};
    return ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN).getFloat();
  }

  private byte[] inputDeviceCommand(InputDeviceSubCommand subCommand, Port port, DeviceType type, DeviceMode mode, int nvalue, int nvalueMultiplier) {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();
    byteCode.addOpCode(CommandType.DIRECT_COMMAND_REPLY);

    // TODO: NOT TESTED when nvalue is more than 2
    byteCode.addGlobalAndLocalBufferSize(nvalue * nvalueMultiplier, 0);
    byteCode.addOpCode(Input.DEVICE);
    byteCode.addOpCode(subCommand);
    byteCode.addParameter(Layer.MASTER);
    byteCode.addParameter(port);
    byteCode.addParameter(type);
    byteCode.addParameter(mode);
    byteCode.addParameter((byte) nvalue); // number of values
    byteCode.addGlobalIndex((byte) 0x00);
    return byteCode.byteArray();
  }

  /**
   * Gets the value in percent from a machine. This method sends a request and receives the result.
   *
   * @param port   the port of a device
   * @param type   the device type
   * @param mode   the mode of the device
   * @param nvalue the number of the response value
   * @return a returned value in percent
   */
  private short[] getPercentValue(Port port, DeviceType type, DeviceMode mode, int nvalue) {
    byte[] command = inputDeviceCommand(InputDeviceSubCommand.READY_PCT, port, type, mode, nvalue, 1);
    // Send message
    mCommunicator.write(command);

    byte[] reply = readAnswer();

    // check the validity of the response
    boolean valid = (reply[2] == CommandType.DIRECT_COMMAND_SUCCESS.code);

    // read the percent value in short type
    short[] result = new short[nvalue];
    for (int i = 0; i < nvalue; i++) {
      result[i] = reply[3 + i];
    }
    return result;
  }

  /**
   * Converts an output port to a byte code port.
   *
   * @param port the port to be converted
   * @return a byte code which expresses a output port
   */
  private byte toByteCodePort(int port) {
    if (port >= 0x00 && port <= 0x03) return (byte) (0x01 << port);
    else return 0x00; // this will not happen
  }

  private int testOutput(Port port) {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();

    // convert port number
    byte byteCodePort = toByteCodePort(port.code);

    byteCode.addOpCode(CommandType.DIRECT_COMMAND_NOREPLY);
    byteCode.addGlobalAndLocalBufferSize(0, 0);

    byteCode.addOpCode(Output.TEST);
    byteCode.addParameter(Layer.MASTER);
    byteCode.addParameter(byteCodePort);

    // send message
    mCommunicator.write(byteCode.byteArray());

    byte[] reply = readAnswer();

    // check the validity of the response
    boolean valid = (reply[2] == CommandType.DIRECT_COMMAND_SUCCESS.code);

    // read the percent value in short type
    return (int) reply[3];
  }

  private void setOutputSpeed(Port port, int speed) {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();

    // convert port number
    byte byteCodePort = toByteCodePort(port.code);

    byteCode.addOpCode(CommandType.DIRECT_COMMAND_NOREPLY);
    byteCode.addGlobalAndLocalBufferSize(0, 0);

    byteCode.addOpCode(Output.SPEED);
    byteCode.addParameter(Layer.MASTER);
    byteCode.addParameter(byteCodePort);
    byteCode.addParameter((byte) speed);

    // send message
    mCommunicator.write(byteCode.byteArray());
  }

  private void setOutputPower(Port port, int power) {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();

    // convert port number
    byte byteCodePort = toByteCodePort(port.code);

    byteCode.addOpCode(CommandType.DIRECT_COMMAND_NOREPLY);
    byteCode.addGlobalAndLocalBufferSize(0, 0);

    byteCode.addOpCode(Output.POWER);
    byteCode.addParameter(Layer.MASTER);
    byteCode.addParameter(byteCodePort);
    byteCode.addParameter((byte) power);

    // send message
    mCommunicator.write(byteCode.byteArray());
  }

  private void stepPower(Port port, int power, int[] steps, int brake) {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();

    // convert port number
    byte byteCodePort = toByteCodePort(port.code);

    byteCode.addOpCode(CommandType.DIRECT_COMMAND_NOREPLY);
    byteCode.addGlobalAndLocalBufferSize(0, 0);

    byteCode.addOpCode(Output.POWER);
    byteCode.addParameter(Layer.MASTER);
    byteCode.addParameter(byteCodePort);
    byteCode.addParameter((byte) power);
    byteCode.addParameter((short) steps[0]);
    byteCode.addParameter((short) steps[1]);
    byteCode.addParameter((short) steps[2]);
    byteCode.addParameter((byte) brake);

    // send message
    mCommunicator.write(byteCode.byteArray());
  }

  private void resetTachoCounts(Port port) {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();

    // convert port number
    byte byteCodePort = toByteCodePort(port.code);

    byteCode.addOpCode(CommandType.DIRECT_COMMAND_NOREPLY);
    byteCode.addGlobalAndLocalBufferSize(0, 0);

    byteCode.addOpCode(Output.RESET);
    byteCode.addParameter(Layer.MASTER);
    byteCode.addParameter(byteCodePort);

    // send message
    mCommunicator.write(byteCode.byteArray());
  }

  /**
   * Makes a sound.
   *
   * @param volume   the volume of a sound (0 ~ 100 [%])
   * @param freq     the frequency [Hz]
   * @param duration the duration of a sound [msec]
   */
  private void soundTone(int volume, int freq, int duration) {
    ByteCodeFormatter byteCode = new ByteCodeFormatter();

    byteCode.addOpCode(CommandType.DIRECT_COMMAND_NOREPLY);
    byteCode.addGlobalAndLocalBufferSize(0, 0);

    byteCode.addOpCode(Sound.SOUND);
    byteCode.addOpCode(SoundSubCommand.TONE);
    byteCode.addParameter((byte) volume);
    byteCode.addParameter((short) freq);
    byteCode.addParameter((short) duration);

    // Send message
    mCommunicator.write(byteCode.byteArray());
  }

  /**
   * Reads data from a machine.
   *
   * @return the returned results
   */
  private byte[] readAnswer() {
    // calculate the size of response by reading 2 bytes
    byte[] header = mCommunicator.read(2);
    int numBytes = ((header[1] & 0x00ff) << 8) | (header[0] & 0x00ff);

    // get result
    byte[] result = mCommunicator.read(numBytes);
    Log.d(TAG, "read: " + result.length + " bytes");

    return result;
  }
}