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
package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices;

import ev3dev.sensors.Battery;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.*;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.Ev3Protocol;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;
import lejos.robotics.RegulatedMotorListener;

import java.util.Map;


/**
 * A base class of a (input / output) device.
 */
public class Ev3RemoteMotor extends Device<DeviceType> implements Ev3RemoteDevice {
  public final ProtocolBase protocol;
  protected final int MAX_SPEED_AT_9V;
  private int speed = 360;
  protected int acceleration = 6000;
  private boolean regulationFlag = true;

  public static Ev3RemoteMotor createLargeMotor(String board, String name, Port port, ProtocolBase protocol) {
    return new Ev3RemoteMotor(board, name, port, protocol, DeviceType.L_MOTOR, 4.0F, 0.04F, 10.0F, 2.0F, 0.02F, 8.0F, 0, 1050);
  }

  public static Ev3RemoteMotor createMediumMotor(String board, String name, Port port, ProtocolBase protocol) {
    return new Ev3RemoteMotor(board, name, port, protocol, DeviceType.M_MOTOR, 8.0F, 0.04F, 8.0F, 8.0F, 0.02F, 0.0F, 1000, 1560);
  }

  private Ev3RemoteMotor(String board, String name, Port port, ProtocolBase protocol, DeviceType device, float moveP, float moveI, float moveD, float holdP, float holdI, float holdD, int offset, int maxSpeed) {
    super(board, name, port, device);
    this.protocol = protocol;
    this.MAX_SPEED_AT_9V = maxSpeed;

//    protocol.exec(Output.SET_MODE, "port", port, "mode", )
      /*this.detect("lego-port", port);
      if (log.isDebugEnabled()) {
        log.debug("Setting port in mode: {}", "tacho-motor");
      }

      this.setStringAttribute("mode", "tacho-motor");
      Delay.msDelay(1000L);
      this.detect("tacho-motor", port);
      Delay.msDelay(1000L);
      this.setStringAttribute("command", "reset");
      if (log.isDebugEnabled()) {
        log.debug("Motor ready to use on Port: {}", motorPort.getName());
      }*/

  }

  public Boolean suspendRegulation() {
    this.regulationFlag = false;
    return this.regulationFlag;
  }

  public Integer getTachoCount() {
    return (Integer) protocol.exec(Output.GET_COUNT, Map.of("port", port)).get(Ev3Protocol.KEY_VALUE);
  }

  public Float getPosition() {
    return (float) this.getTachoCount();
  }

  private void move() {
    if (!this.regulationFlag) {
      protocol.exec(Output.START, Map.of("port", port, "power", this.speed));
    } else {
      protocol.exec(Output.STEP_POWER, Map.of(
          "port", port,
          "power", this.speed,
          "steps", new int[]{this.speed / 6, 2 * this.speed / 3, this.speed / 6}, "break", 1));
    }
  }

  public void forward() {
    this.setSpeedDirect(this.speed);
    move();
  }

  public void backward() {
    this.setSpeedDirect(-this.speed);
    move();
  }

  public void flt(boolean immediateReturn) {
    this.doStop("coast", immediateReturn);
  }

  public void flt() {
    this.flt(false);
  }

  public void coast() {
    this.doStop("coast", false);
  }

  public void brake() {
    this.doStop("brake", false);
  }

  public void hold() {
    this.doStop("hold", false);
  }

  public void stop() {
    this.stop(false);
  }

  public void stop(boolean immediateReturn) {
    this.doStop("hold", immediateReturn);
  }

  private void doStop(String mode, boolean immediateReturn) {
    if (!immediateReturn) {
      throw new UnsupportedOperationException("Cannot stop motors without immediate return");
    }
    switch (mode) {
      case "brake":
        protocol.exec(Output.STOP, Map.of("port", port, "mode", 1));
        break;
      case "coast":
        protocol.exec(Output.STOP, Map.of("port", port, "mode", 0));
        break;
//      case "hold":
//        protocol.exec(Output.STOP, Map.of("port", port));
//        break;
      default:
        throw new IllegalArgumentException("no such mode " + mode);
    }
  }

  public boolean isMoving() {
    return ((int) (protocol.exec(Output.TEST, Map.of("port", port)).get(Ev3Protocol.KEY_VALUE))) == 1;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
    this.setSpeedDirect(speed);
  }

  private void setSpeedDirect(int speed) {
    protocol.exec(Output.POWER, Map.of("port", port, "speed", speed));
  }

  public void resetTachoCount() {
    protocol.exec(Output.RESET, Map.of("port", port));
    this.regulationFlag = true;
  }

  public void rotate(int angle, boolean immediateReturn) {
    if (!immediateReturn) {
      throw new UnsupportedOperationException("Cannot stop motors without immediate return");
    }
    int sign = angle < 0 ? -1 : 1;
    this.setSpeedDirect(this.speed * sign);
    protocol.exec(Output.STEP_POWER, Map.of(
        "port", port,
        "power", this.speed,
        "steps", new int[]{angle / 6, 2 * angle / 3, angle / 6}, "break", 1));
  }

  public void rotate(int angle) {
    this.rotate(angle, false);
  }

  public void rotateTo(int limitAngle, boolean immediateReturn) {
    if (!immediateReturn) {
      throw new UnsupportedOperationException("Cannot stop motors without immediate return");
    }
    int angle;
    if (limitAngle > this.getTachoCount()) {
      angle = limitAngle - this.getTachoCount();
      this.setSpeedDirect(this.speed);
    } else {
      angle = this.getTachoCount() - limitAngle;
      this.setSpeedDirect(-this.speed);
    }
    protocol.exec(Output.STEP_POWER, Map.of(
        "port", port,
        "power", this.speed,
        "steps", new int[]{angle / 6, 2 * angle / 3, angle / 6}, "break", 1));
  }

  public void rotateTo(int limitAngle) {
    this.rotateTo(limitAngle, false);
  }

  public int getSpeed() {
    String type = device.name;
    return (int) (!this.regulationFlag ?
        protocol.exec(InputDeviceSubCommand.READY_PCT, Map.of("port", port, "mode", DeviceMode.get(device, type + "-SPD"))).get(Ev3Protocol.KEY_VALUE)
        :
        protocol.exec(InputDeviceSubCommand.READY_SI, Map.of("port", port, "mode", DeviceMode.get(device, type + "-ROT"))).get(Ev3Protocol.KEY_VALUE));

    // !this.regulationFlag ? duty_cycle_sp (pct):speed_sp (tacho counts per second)
  }

  public boolean isStalled() {
    return isMoving() && getSpeed() == 0;
  }

  public int getRotationSpeed() {
    return 0;
  }

  public void addListener(RegulatedMotorListener regulatedMotorListener) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public RegulatedMotorListener removeListener() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void waitComplete() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public float getMaxSpeed() {
    return
        ((float) protocol.exec(UIReadSubCommand.GET_VBATT, Map.of()).get(Ev3Protocol.KEY_VALUE))
            * (float) this.MAX_SPEED_AT_9V / 9.0F * 0.9F;
  }

  public void setAcceleration(int acceleration) {
    this.acceleration = Math.abs(acceleration);
  }

  @Override
  public ProtocolBase getProtocol() {
    return protocol;
  }
}
