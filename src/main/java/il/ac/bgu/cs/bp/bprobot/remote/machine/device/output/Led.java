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
package il.ac.bgu.cs.bp.bprobot.remote.machine.device.output;


import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.RemoteDevice;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.OutputPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;

import static il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.Ev3Constants.UI_WRITE_CMD_LED;

/**
 * A LED class.
 */
public class Led extends RemoteDevice {

  public Led(OutputPort port, ProtocolBase protocol) {
    super(port, UI_WRITE_CMD_LED, protocol);
  }

  /**
   * Turns on this LED if it is off.
   */
  public void turnOn() {
    CommandBase cmd = CommandFactory.createCommand(CommandType.SET_LED_ON, null);
    exec(cmd);
  }

  /**
   * Turns off this LED if it is on.
   */
  public void turnOff() {
    CommandBase cmd = CommandFactory.createCommand(CommandType.SET_LED_OFF, null);
    exec(cmd);
  }
}
