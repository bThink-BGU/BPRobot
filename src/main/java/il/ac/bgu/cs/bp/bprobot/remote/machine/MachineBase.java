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
package il.ac.bgu.cs.bp.bprobot.remote.machine;

import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.ColorSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.GyroSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.LightSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.Rangefinder;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.RemoteControlReceiver;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.SoundSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.TouchSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Buzzer;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Led;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Motor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Servomotor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePort;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.OutputPort;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;

import java.io.IOException;

/**
 * A base class for machines that specifies interfaces of a machine.
 * This class also be used as a factory class to generate sensors/motors.
 */
public abstract class MachineBase extends Board<DevicePort> {
    protected ProtocolBase mProtocol;

    public MachineBase(String name, ProtocolBase protocol) {
        super(name);
        mProtocol = protocol;
    }

    /**
     * Connects to this machine.
     *
     * @throws IOException if failed to open a connection
     */
    public void connect() throws IOException {
        mProtocol.open();
    }

    /**
     * Disconnects from this machine.
     */
    public void disconnect() {
        mProtocol.close();
    }

    /**
     * Applies the commands as a transaction (for PILE machines).
     */
    public void apply() {
        throw new UnsupportedOperationException("This machine does not support 'apply' command");
    }

    /**
     * Loads data from this machine.
     *
     * @param key a key of a key-value store
     * @return raw values in an array of bytes
     */
    public byte[] load(int key) {
        throw new UnsupportedOperationException("This machine does not support 'load' command");
    }

    /**
     * Stores data to this machine.
     *
     * @param key a key of a key-value store
     * @param data a value of a key-value store
     * @return succeed (<code>true</code>) or (<code>false</code>)
     */
    public boolean store(int key, byte[] data) {
        throw new UnsupportedOperationException("This machine does not support 'store' command");
    }

    /**
     * Creates a {@link Motor}.
     *
     * @param port an {@link OutputPort} which is supposed to be bounded
     * @return a {@link Motor} which is connected to the specified port
     */
    public Motor createMotor(OutputPort port) {
        throw new UnsupportedOperationException("This machine does not support Motor");
    }

    /**
     * Creates a {@link Servomotor}.
     *
     * @param port an {@link OutputPort} which is supposed to be bounded
     * @return a {@link Servomotor} which is connected to the specified port
     */
    public Servomotor createServomotor(OutputPort port) {
        throw new UnsupportedOperationException("This machine does not support Servomotor");
    }

    /**
     * Creates a {@link Buzzer}.
     *
     * @param port an {@link OutputPort} which is supposed to be bounded
     * @return a {@link Buzzer} which is connected to the specified port
     */
    public Buzzer createBuzzer(OutputPort port) {
        throw new UnsupportedOperationException("This machine does not support Buzzer");
    }

    /**
     * Creates a {@link Led}.
     *
     * @param port an {@link OutputPort} which is supposed to be bounded
     * @return a {@link Led} which is connected to the specified port
     */
    public Led createLed(OutputPort port) {
        throw new UnsupportedOperationException("This machine does not support LED");
    }

    /**
     * Creates a {@link LightSensor}.
     *
     * @param port an {@link InputPort} which is supposed to be bounded
     * @return a {@link LightSensor} which is connected to the specified port
     */
    public LightSensor createLightSensor(InputPort port) {
        throw new UnsupportedOperationException("This machine does not support LightSensor");
    }

    /**
     * Creates a {@link GyroSensor}.
     *
     * @param port an {@link InputPort} which is supposed to be bounded
     * @return a {@link GyroSensor} which is connected to the specified port
     */
    public GyroSensor createGyroSensor(InputPort port) {
        throw new UnsupportedOperationException("This machine does not support GyroSensor");
    }

    /**
     * Creates a {@link TouchSensor}.
     *
     * @param port an {@link InputPort} which is supposed to be bounded
     * @return a {@link TouchSensor} which is connected to the specified port
     */
    public TouchSensor createTouchSensor(InputPort port) {
        throw new UnsupportedOperationException("This machine does not support TouchSensor");
    }

    /**
     * Creates a {@link ColorSensor}.
     *
     * @param port an {@link InputPort} which is supposed to be bounded
     * @return a {@link ColorSensor} which is connected to the specified port
     */
    public ColorSensor createColorSensor(InputPort port) {
        throw new UnsupportedOperationException("This machine does not support ColorSensor");
    }

    /**
     * Creates a {@link Rangefinder}.
     *
     * @param port an {@link InputPort} which is supposed to be bounded
     * @return a {@link Rangefinder} which is connected to the specified port
     */
    public Rangefinder createRangefinder(InputPort port) {
        throw new UnsupportedOperationException("This machine does not support Rangefinder");
    }

    /**
     * Creates a {@link SoundSensor}.
     *
     * @param port an {@link InputPort} which is supposed to be bounded
     * @return a {@link SoundSensor} which is connected to the specified port
     */
    public SoundSensor createSoundSensor(InputPort port) {
        throw new UnsupportedOperationException("This machine does not support SoundSensor");
    }

    /**
     * Creates a {@link RemoteControlReceiver}.
     *
     * @param port an {@link InputPort} which is supposed to be bounded
     * @return a {@link RemoteControlReceiver} which is connected to the specified port
     */
    public RemoteControlReceiver createRemoteControlReceiver(InputPort port) {
        throw new UnsupportedOperationException("This machine does not support RemoteControlReceiver");
    }
}
