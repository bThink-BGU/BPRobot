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

/**
 * A container class that has values for a sensor of LEGO MINDSTORMS NXT.
 */
public class InputValues {
    public int inputPort;
    public boolean valid = true;
    public boolean isCalibrated;
    public int sensorType;
    public int sensorMode;
    /**
     * The raw value from the Analog to Digital (AD) converter.
     */
    public int rawADValue;
    /**
     * The normalized value from the Analog to Digital (AD) converter.
     */
    public int normalizedADValue;
    /**
     * The scaled value starts working after the first call to the sensor.
     * The first value will be the raw value, but after that it produces scaled values.
     * With the touch sensor, off scales to 0 and on scales to 1.
     */
    public short scaledValue;
    /**
     * Currently unused.
     */
    public short calibratedValue;
}
