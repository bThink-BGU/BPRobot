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
package il.ac.bgu.cs.bp.bprobot.remote.model.nxt;

import il.ac.bgu.cs.bp.bprobot.remote.machine.MachineBase;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.LightSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.SoundSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.input.TouchSensor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.output.Motor;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePort;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePortTypeMismatchException;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.OutputPort;
import il.ac.bgu.cs.bp.bprobot.remote.model.com.ICommunicator;
import il.ac.bgu.cs.bp.bprobot.remote.model.nxt.port.NxtInputPort;
import il.ac.bgu.cs.bp.bprobot.remote.model.nxt.port.NxtOutputPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.DeviceWrapper;

/**
 * A machine class for LEGO MINDSTORMS NXT.
 */
public class NxtMachine extends MachineBase {

    public NxtMachine(String name, ICommunicator comm) {
        super(name, new NxtProtocol(comm));
    }

    @Override
    public Motor createMotor(OutputPort port) {
        checkOutputPortCompatibility(port);
        return new Motor(port, mProtocol);
    }

    @Override
    public LightSensor createLightSensor(InputPort port) {
        checkInputPortCompatibility(port);
        return new LightSensor(port, mProtocol);
    }

    @Override
    public TouchSensor createTouchSensor(InputPort port) {
        checkInputPortCompatibility(port);
        return new TouchSensor(port, mProtocol);
    }

    @Override
    public SoundSensor createSoundSensor(InputPort port) {
        checkInputPortCompatibility(port);
        return new SoundSensor(port, mProtocol);
    }

    private void checkOutputPortCompatibility(OutputPort port) {
        if (port instanceof NxtOutputPort) {
            return;
        }

        throw new DevicePortTypeMismatchException("Expected: NxtOutputPort, Actual: " + port.getClass());
    }

    private void checkInputPortCompatibility(InputPort port) {
        if (port instanceof NxtInputPort) {
            return;
        }

        throw new DevicePortTypeMismatchException("Expected: NxtInputPort, Actual: " + port.getClass());
    }

    @Override
    public DevicePort getPort(String portName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected DeviceWrapper<?> createDeviceWrapper(String nickname, DevicePort port, String type, Object... ctorParams) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
