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

import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;
import il.ac.bgu.cs.bp.bprobot.remote.model.com.ICommunicator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static il.ac.bgu.cs.bp.bprobot.remote.model.nxt.NxtConstants.*;

/**
 * A protocol class for LEGO MINDSTORMS NXT.
 */
public class NxtProtocol extends ProtocolBase {
    private static final String KEY_VALUE = "value";
    private static final String TAG = "NxtProtocol";
    private static final int MAX_RES_LENGTH = 66;
    private Map<Integer, Byte> mPortTypes;

    public NxtProtocol(ICommunicator comm) {
        super(comm);
    }

    @Override
    public void open() throws IOException {
        mCommunicator.open();
        mPortTypes = new HashMap<>();
    }

    @Override
    public void close() {
        mCommunicator.close();
        mPortTypes = null;
    }

    @Override
    public Map<String, Object> exec(int port, CommandBase cmd) {
        Map<String, Object> res = new HashMap<>();
        CommandType type = cmd.getCommandType();
        switch (type) {
            case GET_LIGHT_VALUE: {
                setInputMode(port, LIGHT_ACTIVE, PCTFULLSCALEMODE);
                InputValues values = getInputValues(port);
                res.put(KEY_VALUE, values.scaledValue / 10);
                break;
            }
            case GET_SOUND_DB: {
                setInputMode(port, SOUND_DB, PCTFULLSCALEMODE);
                InputValues values = getInputValues(port);
                res.put(KEY_VALUE, values.scaledValue / 10);
                break;
            }
            case GET_TOUCH_TOUCHED: {
                setInputMode(port, SWITCH, BOOLEANMODE);
                InputValues values = getInputValues(port);
                res.put(KEY_VALUE, values.scaledValue < 600);
                break;
            }
            case SET_MOTOR_SPEED: {
                Map<String, Object> args = cmd.getArgs();
                int speed = (Integer) args.get("speed");
                setOutputState(port, speed, BRAKE + MOTORON + REGULATED,
                        REGULATION_MODE_MOTOR_SPEED, 0, MOTOR_RUN_STATE_RUNNING, 0);
                break;
            }

            case GET_COLOR_ILLUMINANCE:
            case GET_COLOR_RGB:
            case GET_GYRO_ANGLE:
            case GET_GYRO_RATE:
            case GET_RANGEFINDER_DIST:
            case GET_REMOTECONTROLLER_BUTTON:
            case GET_REMOTECONTROLLER_DIST:
            case GET_SERVO_ANGLE:
            case GET_TOUCH_COUNT:
            case SET_BUZZER_BEEP:
            case SET_BUZZER_OFF:
            case SET_BUZZER_ON:
            case SET_LED_OFF:
            case SET_LED_ON:
            case SET_SERVO_ANGLE: {
                throw new UnsupportedOperationException(type.name() + " Operation hasn't been implemented yet");
            }

            default: {
                throw new UnsupportedOperationException("This Operation hasn't been implemented yet");
            }
        }
        return res;
    }

    /**
     * Sets the status of an output device.
     *
     * @param port the port of a device (0 ~ 3)
     * @param speed the speed of an output device
     * @param mode the mode of an output device (ex. MOTORON, BRAKE, and/or REGULATED). This field is a bit field.
     * @param regulationMode see {@link NxtConstants}
     * @param turnRatio Use this parameter to move more than two motors
     * @param runState see {@link NxtConstants}
     * @param tachoLimit the number of degrees to rotate before stopping
     */
    private void setOutputState(int port, int speed, int mode,
                                int regulationMode, int turnRatio, int runState, int tachoLimit) {
        byte[] request = {
                DIRECT_COMMAND_NOREPLY,
                SET_OUTPUT_STATE,
                (byte) port,
                (byte) speed,
                (byte) mode,
                (byte) regulationMode,
                (byte) turnRatio,
                (byte) runState,
                (byte) tachoLimit,
                (byte) (tachoLimit >>> 8),
                (byte) (tachoLimit >>> 16),
                (byte) (tachoLimit >>> 24)
        };
        // send request
        sendData(request);
    }

    /**
     * Sends data to a machine. This method calculates the size of the data and append it to the data.
     *
     * @param request a request to be sent to a machine
     */
    private void sendData(byte[] request) {
        // Calculate the size of request and append them
        byte[] data = new byte[request.length + 2];
        data[0] = (byte) request.length;
        data[1] = (byte) (request.length >> 8);
        System.arraycopy(request, 0, data, 2, request.length);

        // Send request
        mCommunicator.write(data);
    }

    private InputValues getInputValues(int port) {
        byte[] request = {
                DIRECT_COMMAND_REPLY,
                GET_INPUT_VALUES,
                (byte) port
        };
        sendData(request);
        byte[] reply = mCommunicator.read(MAX_RES_LENGTH);
        InputValues inputValues = new InputValues();
        inputValues.inputPort = reply[3];
        // 0 is false, 1 is true
        inputValues.valid = (reply[4] != 0);
        // 0 is false, 1 is true
        inputValues.isCalibrated = (reply[5] == 0);
        inputValues.sensorType = reply[6];
        inputValues.sensorMode = reply[7];
        inputValues.rawADValue = (short) ((0xFF & reply[8]) | ((0xFF & reply[9]) << 8));
        inputValues.normalizedADValue = (short) ((0xFF & reply[10]) | ((0xFF & reply[11]) << 8));
        inputValues.scaledValue = (short) ((0xFF & reply[12]) | ((0xFF & reply[13]) << 8));
        inputValues.calibratedValue = (short) ((0xFF & reply[14]) | ((0xFF & reply[15]) << 8));

        return inputValues;
    }

    /**
     * Tells an NXT what type of sensor is used and the mode to operate in.
     *
     * @param port the port of a device (0 ~ 3)
     * @param sensorType see {@link NxtConstants}
     * @param sensorMode see {@link NxtConstants}
     */
    public void setInputMode(int port, int sensorType, int sensorMode) {
        // if the port has not been initialized yet, set the mode
        if (!mPortTypes.containsKey(port) || sensorType != mPortTypes.get(port)) {
            // save the port setting to a map and set the mode of sensor
            mPortTypes.put(port, (byte) sensorType);

            byte[] request = {
                    DIRECT_COMMAND_NOREPLY,
                    SET_INPUT_MODE,
                    (byte) port,
                    (byte) sensorType,
                    (byte) sensorMode
            };

            sendData(request);
            getInputValues(port); // skip the first value (it may be invalid)

            // sound sensor needs more initializing
            // time based on our experiments
            if (sensorType == SOUND_DB) waitMillSeconds(250);
        }
    }

    /**
     * A helper method to wait for specified milli seconds.
     *
     * @param milliseconds time to wait in millisecond
     */
    private void waitMillSeconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

