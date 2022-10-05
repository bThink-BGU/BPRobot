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
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandFactory;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;

import java.util.Map;

import static il.ac.bgu.cs.bp.bprobot.remote.model.ev3.Ev3Constants.EV3_GYRO;

/**
 * A gyro sensor class.
 */
public class GyroSensor extends RemoteDeviceBase {

    public GyroSensor(InputPort port, ProtocolBase protocol) {
        super(port, EV3_GYRO, protocol);
    }

    /**
     * Gets the rate of rotations.
     *
     * @return the current rate
     */
    public int getRate() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_GYRO_RATE, null);
        Map<String, Object> res = exec(cmd);
        return (Integer) res.get("value");
    }

    /**
     * Gets the angle of this gyro sensor.
     *
     * @return the current angle
     */
    public int getAngle() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_GYRO_ANGLE, null);
        Map<String, Object> res = exec(cmd);
        return (Integer) res.get("value");
    }
}
