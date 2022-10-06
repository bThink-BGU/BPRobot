/**
 * Copyright (C) 2011-2017 The PILE Developers <pile-dev@googlegroups.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package il.ac.bgu.cs.bp.bprobot.remote.machine.device.input;

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.RemoteDevice;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;

import java.util.Map;

import static il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.Ev3Constants.EV3_TOUCH;

/**
 * A touch sensor class.
 */
public class TouchSensor extends RemoteDevice {

    public TouchSensor(InputPort port, ProtocolBase protocol) {
        super(port, EV3_TOUCH, protocol);
    }

    /**
     * Checks if this sensor is touched now.
     *
     * @return touched (<code>true</code>) or (<code>false</code>)
     */
    public boolean isTouched() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_TOUCH_TOUCHED, null);
        Map<String, Object> res = exec(cmd);
        return (Boolean) res.get("value");
    }

    /**
     * Gets the touched count.
     *
     * @return the number of touched count
     */
    public int getTouchedCount() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_TOUCH_COUNT, null);
        Map<String, Object> res = exec(cmd);
        return (Integer) res.get("value");
    }
}
