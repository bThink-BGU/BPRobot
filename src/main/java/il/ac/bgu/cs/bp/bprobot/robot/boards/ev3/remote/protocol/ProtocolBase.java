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
package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol;

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.CommandBase;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.IRemoteAction;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.communication.ICommunicator;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.Ev3RemoteMotor;

import java.io.IOException;
import java.util.Map;

/**
 * A base class of protocols which are used for
 * {@link Ev3RemoteMotor}.
 * to interpret machine-specific byte codes.
 */
public abstract class ProtocolBase {
    protected final ICommunicator mCommunicator;

    public ProtocolBase(ICommunicator comm) {
        mCommunicator = comm;
    }

    /**
     * Opens a connection between devices.
     *
     * @throws IOException if failed to open the connection
     */
    public abstract void open() throws IOException;

    /**
     * Closes the connection between devices.
     */
    public abstract void close();

    /**
     * Executes a {@link CommandBase} with a port and returns the result as a map.
     *
     * @param cmd a {@link CommandBase} which is to be executed
     * @param args arguments of the command
     * @return the result of the command
     */
    public abstract Map<String, Object> exec(IRemoteAction cmd, Map<String, Object> args);
}
