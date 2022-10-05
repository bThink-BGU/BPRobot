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

import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePort;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;

import java.util.Map;

/**
 * A base class of a (input / output) device.
 */
public abstract class RemoteDeviceBase {
  private final DevicePort mPort;
  private final ProtocolBase mProtocol;
  public final byte type;

  /**
   * @param port a port where this device is to be inserted
   * @param protocol a protocol of a machine
   */
  public RemoteDeviceBase(DevicePort port, byte type, ProtocolBase protocol) {
    mPort = port;
    this.type = type;
    mProtocol = protocol;
  }

  /**
   * Executes a command.
   *
   * @param command a command to be executed
   * @return the returned result as a map
   */
  public Map<String, Object> exec(CommandBase command) {
    return mProtocol.exec(mPort.getRaw(), command);
  }
}
