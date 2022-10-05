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

import il.ac.bgu.cs.bp.bprobot.remote.machine.device.DeviceBase;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.DeviceType;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.OutputPort;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandFactory;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;

import java.util.HashMap;
import java.util.Map;

/**
 * A motor class.
 */
public class Motor extends DeviceBase {
    private int mSpeed = 50; // initial value

    public Motor(OutputPort port, ProtocolBase protocol) {
        super(port, protocol);
    }

    /**
     * Moves this motor forward.
     */
    public void forward() {
        Map<String, Object> args = new HashMap<>();
        args.put("speed", mSpeed);
        CommandBase cmd = CommandFactory.createCommand(CommandType.SET_MOTOR_SPEED, args);
        exec(cmd);
    }

    /**
     * Moves this motor backward.
     */
    public void backward() {
        Map<String, Object> args = new HashMap<>();
        args.put("speed", -mSpeed);
        CommandBase cmd = CommandFactory.createCommand(CommandType.SET_MOTOR_SPEED, args);
        exec(cmd);
    }

    /**
     * Stops this motor.
     */
    public void stop() {
        Map<String, Object> args = new HashMap<>();
        args.put("speed", 0);
        CommandBase cmd = CommandFactory.createCommand(CommandType.SET_MOTOR_SPEED, args);
        exec(cmd);
    }

    /**
     * Gets the speed of this motor.
     *
     * @return speed (0 - 100)
     */
    public int getSpeed() {
        return mSpeed;
    }

    /**
     * Sets the speed (0 - 100) of this motor.
     * If the speed is out of the range,
     * this method do nothing.
     *
     * @param speed a speed to be set to this motor
     */
    public void setSpeed(int speed) {
        if (speed < 0 || speed > 100) return;
        mSpeed = speed;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.MOTOR;
    }

}
