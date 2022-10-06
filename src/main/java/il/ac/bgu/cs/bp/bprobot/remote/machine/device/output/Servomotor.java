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
package il.ac.bgu.cs.bp.bprobot.remote.machine.device.output;

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.RemoteDevice;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.OutputPort;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;

import java.util.HashMap;
import java.util.Map;

import static il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.Ev3Constants.L_MOTOR;

/**
 * A servomotor class.
 */
public class Servomotor extends RemoteDevice {

    public Servomotor(OutputPort port, ProtocolBase protocol) {
        super(port, L_MOTOR, protocol);
    }

    /**
     * Gets the angle of this servomotor.
     *
     * @return the current angle
     */
    public int getAngle() {
        CommandBase cmd = CommandFactory.createCommand(CommandType.GET_SERVO_ANGLE, null);
        Map<String, Object> res = exec(cmd);
        return (Integer) res.get("value");
    }

    /**
     * Sets the angle to this servomotor.
     *
     * @param angle an angle to be set
     */
    public void setAngle(int angle) {
        Map<String, Object> args = new HashMap<>();
        args.put("angle", angle);
        CommandBase cmd = CommandFactory.createCommand(CommandType.SET_SERVO_ANGLE, args);
        exec(cmd);
    }
}
