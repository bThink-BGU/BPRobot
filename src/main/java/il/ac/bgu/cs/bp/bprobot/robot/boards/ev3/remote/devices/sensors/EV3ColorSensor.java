/**
 * Copyright (C) 2011-2017 The PILE Developers <pile-dev@googlegroups.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.sensors;

import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.InputPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;
import il.ac.bgu.cs.bp.bprobot.robot.boards.GenericSensorMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.Input;
import lejos.hardware.sensor.SensorMode;

import static il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.Ev3Constants.EV3_COLOR;

/**
 * A color sensor class.
 */
public class EV3ColorSensor extends Ev3RemoteSensor {
  private static final String LEGO_EV3_COLOR_SENSOR = "lego-ev3-color";
  private static final String COL_COLOR = "COL-COLOR";
  private static final String COL_REFLECT = "COL-REFLECT";
  private static final String COL_AMBIENT = "COL-AMBIENT";
  private static final String COL_RGBRAW = "RGB-RAW";
  private static final String COL_REFRAW = "REF-RAW";
  private static final String COL_CAL = "COL-CAL";

  public EV3ColorSensor(String board, String name, InputPort port, ProtocolBase protocol) {
    super(board, name, port, protocol, EV3_COLOR, null,
        new GenericSensorMode(1, "ColorID", Input.SI),
        new GenericSensorMode(1, "Red", Input.SI),
        new GenericSensorMode(1, "Ambient", Input.SI),
        new GenericSensorMode(3, "RGB", Input.SI)
    );
  }
  public int getColorID() {
    float[] sample = new float[1];
    this.getColorIDMode().fetchSample(sample, 0);
    return (int)sample[0];
  }

  public boolean isFloodlightOn() {
    return this.getFloodlight() != 0;
  }

  public int getFloodlight() {
    switch (this.getSystemM) {
      case "COL-COLOR":
      case "RGB-RAW":
        return 6;
      case "REF-RAW":
      case "COL-REFLECT":
        return 5;
      case "COL-AMBIENT":
      case "COL-CAL":
      default:
        return 0;
    }
  }

  public void setFloodlight(boolean floodlight) {
    this.setFloodlight(floodlight ? 5 : 2);
  }

  public boolean setFloodlight(int color) {
    String mode;
    switch (color) {
      case 2:
        mode = "COL-AMBIENT";
        break;
      case 3:
      case 4:
      default:
        throw new IllegalArgumentException("Invalid color specified");
      case 5:
        mode = "COL-REFLECT";
        break;
      case 6:
        mode = "COL-COLOR";
    }

    this.switchMode(mode, 400L);
    return true;
  }

  public SensorMode getColorIDMode() {
    this.switchMode("COL-COLOR", 400L);
    return this.getMode(0);
  }

  public SensorMode getRedMode() {
    this.switchMode("COL-REFLECT", 400L);
    return this.getMode(1);
  }

  public SensorMode getAmbientMode() {
    this.switchMode("COL-AMBIENT", 400L);
    return this.getMode(2);
  }

  public SensorMode getRGBMode() {
    this.switchMode("RGB-RAW", 400L);
    return this.getMode(3);
  }
}
