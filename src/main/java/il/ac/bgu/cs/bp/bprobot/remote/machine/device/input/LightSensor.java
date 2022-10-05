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

import il.ac.bgu.cs.bp.bprobot.remote.machine.device.DeviceBase;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.DeviceType;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandFactory;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;

import java.util.Map;

/**
 * A light sensor class.
 */
public class LightSensor extends DeviceBase {

    public LightSensor(InputPort port, ProtocolBase protocol) {
        super(port, protocol);
    }

    /**
     * Gets the value of light sensor.
     *
     * @return sensor value (0 - 100%)
     */
    public int getSensorValue() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_LIGHT_VALUE, null);
        Map<String, Object> value = exec(cmd);
        return (Integer) value.get("value");
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.LIGHT_SENSOR;
    }
}
