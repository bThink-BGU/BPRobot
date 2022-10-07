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

import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.DeviceType;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.Port;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;


/**
 * A base class of a (input / output) device.
 */
public class Ev3RemoteMotor extends Device<DeviceType> implements Ev3RemoteDevice{
  public final ProtocolBase protocol;

  public Ev3RemoteMotor(String board, String name, Port port, ProtocolBase protocol, DeviceType device) {
    super(board, name, port, device);
    this.protocol = protocol;
  }

  @Override
  public ProtocolBase getProtocol() {
    return protocol;
  }

  @Override
  public void runVoidMethod(String name, Object... params) throws NoSuchMethodException {
    try{
      super.runVoidMethod(name, params);
    } catch (NoSuchMethodException e) {
      device.execute(this, name, params);
    }

  }

  @Override
  public <V> V runMethod(String name, Object... params) throws NoSuchMethodException {
    try{
      return super.runMethod(name, params);
    } catch (NoSuchMethodException e) {
      return device.execute(this, name, params);
    }
  }
}
