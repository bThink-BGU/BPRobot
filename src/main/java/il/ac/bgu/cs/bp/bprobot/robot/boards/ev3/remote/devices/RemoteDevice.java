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

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.CommandBase;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.Port;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;

import java.util.HashMap;
import java.util.Map;

/**
 * A base class of a (input / output) device.
 */
public class RemoteDevice {
  protected final Port port;
  protected final ProtocolBase protocol;
  protected final byte type;
  private final Map<String, CommandBase> commands = new HashMap<>();

  public RemoteDevice(Port port, ProtocolBase protocol, byte type) {
    this.port = port;
    this.protocol = protocol;
    this.type = type;
  }

  public Map<String, Object> exec(CommandBase command) {
    return protocol.exec(port, command);
  }

  public int getSystemMode() {
    var cmd = CommandFactory.createCommand(CommandType.GET_SYSTEM_TYPE_MODE, Map.of());
    var res = exec(cmd);
    return (int) res.get("mode");
  }
}
