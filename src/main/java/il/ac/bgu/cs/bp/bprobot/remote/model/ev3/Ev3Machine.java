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
package il.ac.bgu.cs.bp.bprobot.remote.model.ev3;

import il.ac.bgu.cs.bp.bprobot.remote.machine.MachineBase;
import il.ac.bgu.cs.bp.bprobot.remote.machine.MachineStatus;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.DeviceType;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.*;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Buzzer;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Led;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Motor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Servomotor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePort;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePortTypeMismatchException;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.OutputPort;
import il.ac.bgu.cs.bp.bprobot.remote.model.com.ICommunicator;
import il.ac.bgu.cs.bp.bprobot.remote.model.ev3.port.Ev3InputPort;
import il.ac.bgu.cs.bp.bprobot.remote.model.ev3.port.Ev3OutputPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.DeviceWrapper;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.internals.EV3DevPort;

import java.util.Map;

/**
 * A machine class for LEGO MINDSTORMS EV3.
 */
public class Ev3Machine extends MachineBase {
    private static final Map<String, DevicePort> portsMap = Map.of(
        "S1", Ev3InputPort.PORT_1,
        "S2", Ev3InputPort.PORT_2,
        "S3", Ev3InputPort.PORT_3,
        "S4", Ev3InputPort.PORT_4,
        "A", Ev3OutputPort.PORT_A,
        "B", Ev3OutputPort.PORT_B,
        "C", Ev3OutputPort.PORT_C,
        "D", Ev3OutputPort.PORT_D
    );

    public Ev3Machine(String name, ICommunicator comm) {
        super(name, new Ev3Protocol(comm));
    }

    @Override
    public void apply() {
        mProtocol.apply();
    }

    @Override
    public MachineStatus fetchStatus() {
        return mStatus;
    }

    @Override
    public Motor createMotor(OutputPort port) {
        checkOutputPortCompatibility(port);

        mStatus.bind(port, DeviceType.MOTOR);
        return new Motor(port, mProtocol);
    }

    @Override
    public Servomotor createServomotor(OutputPort port) {
        checkOutputPortCompatibility(port);

        mStatus.bind(port, DeviceType.SERVOMOTOR);
        return new Servomotor(port, mProtocol);
    }

    @Override
    public Buzzer createBuzzer(OutputPort port) {
        checkOutputPortCompatibility(port);

        mStatus.bind(port, DeviceType.BUZZER);
        return new Buzzer(port, mProtocol);
    }

    @Override
    public Led createLed(OutputPort port) {
        checkOutputPortCompatibility(port);

        mStatus.bind(port, DeviceType.LED);
        return new Led(port, mProtocol);
    }

    @Override
    public LightSensor createLightSensor(InputPort port) {
        checkInputPortCompatibility(port);

        mStatus.bind(port, DeviceType.LIGHT_SENSOR);
        return new LightSensor(port, mProtocol);
    }

    @Override
    public TouchSensor createTouchSensor(InputPort port) {
        checkInputPortCompatibility(port);

        mStatus.bind(port, DeviceType.TOUCH_SENSOR);
        return new TouchSensor(port, mProtocol);
    }

    @Override
    public SoundSensor createSoundSensor(InputPort port) {
        checkInputPortCompatibility(port);

        mStatus.bind(port, DeviceType.SOUND_SENSOR);
        return new SoundSensor(port, mProtocol);
    }

    @Override
    public GyroSensor createGyroSensor(InputPort port) {
        checkInputPortCompatibility(port);

        mStatus.bind(port, DeviceType.GYRO_SENSOR);
        return new GyroSensor(port, mProtocol);
    }

    @Override
    public ColorSensor createColorSensor(InputPort port) {
        checkInputPortCompatibility(port);

        mStatus.bind(port, DeviceType.COLOR_SENSOR);
        return new ColorSensor(port, mProtocol);
    }

    @Override
    public Rangefinder createRangefinder(InputPort port) {
        checkInputPortCompatibility(port);

        mStatus.bind(port, DeviceType.RANGEFINDER);
        return new Rangefinder(port, mProtocol);
    }

    @Override
    public RemoteControlReceiver createRemoteControlReceiver(InputPort port) {
        checkInputPortCompatibility(port);

        mStatus.bind(port, DeviceType.REMOTECONTROL_RECEIVER);
        return new RemoteControlReceiver(port, mProtocol);
    }

    private void checkInputPortCompatibility(InputPort port) {
        if (port instanceof Ev3InputPort) {
            return;
        }

        throw new DevicePortTypeMismatchException("Expected: Ev3InputPort class, Actual: " + port);
    }

    private void checkOutputPortCompatibility(OutputPort port) {
        if (port instanceof Ev3OutputPort) {
            return;
        }

        throw new DevicePortTypeMismatchException("Expected: Ev3OutputPort class, Actual: " + port);
    }

    @Override
    public DevicePort getPort(String portName) {
        var port = portsMap.get(portName);
        if (port == null) throw new IllegalArgumentException("No such port " + portName);
        return port;
    }

    @Override
    protected DeviceWrapper<?> createDeviceWrapper(String nickname, DevicePort port, String type, Object... ctorParams) throws Exception {
        return null;
    }
}
