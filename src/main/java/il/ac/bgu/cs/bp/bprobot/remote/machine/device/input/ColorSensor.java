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

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.RemoteDeviceBase;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.DeviceType;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandFactory;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;

import java.util.Map;

import static il.ac.bgu.cs.bp.bprobot.remote.model.ev3.Ev3Constants.EV3_COLOR;

/**
 * A color sensor class.
 */
public class ColorSensor extends RemoteDeviceBase {

    public ColorSensor(InputPort port, ProtocolBase protocol) {
        super(port, EV3_COLOR, protocol);
    }

    /**
     * Gets the color in RGB (0 - 255)
     * TODO: this method doesn't return proper value.
     *
     * @return RGB values in an array of float ([0]: r, [1]: g, [2]: b)
     */
    public float[] getRgb() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_COLOR_RGB, null);
        Map<String, Object> res = exec(cmd);
        return (float[]) res.get("value");
    }

    /**
     * Gets the illuminance in percent (0 - 100)
     *
     * @return the illuminance (0 - 100%)
     */
    public int getIlluminance() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_COLOR_ILLUMINANCE, null);
        Map<String, Object> res = exec(cmd);
        return (Integer) res.get("value");
    }
}
