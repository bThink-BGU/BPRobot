package il.ac.bgu.cs.bp.bprobot.robot.boards;

import ev3dev.sensors.GenericMode;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class SensorWrapper<T> extends DeviceWrapper<T> implements SensorModes {
  private ArrayList<String> modeList;
  protected int currentMode = 0;
  protected SensorMode[] modes;

  protected SensorWrapper(String board, String name, Port port, T device, SensorMode ... modes) {
    super(board, name, port, device);
    if (modes == null || modes.length == 0) modes = new GenericSensorMode[]{new GenericSensorMode()};
    setModes(modes);
  }

  protected abstract void sample(float[] sample) throws Exception;

  /**
   * Set the current SensorMode index.
   *
   * <p><b>WARNING:</b> this function works properly only when
   * the sensor is already in the appropriate mode. This means
   * that the returned reading will be valid only when
   * you previously activated the "current mode" via a call
   * to get*Mode() or switchMode().
   * See {@link GenericMode#fetchSample(float[], int)}</p>
   *
   * @param sample The array to store the sample in.
   * @param offset The elements of the sample are stored in the array starting at the offset position.
   */
  @Override
  public void fetchSample(float[] sample, int offset) {
    try {
      sample(sample);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    modes[currentMode].fetchSample(sample, offset);
  }

  /**
   * Define the set of modes to be made available for this sensor.
   *
   * @param modes An array containing a list of modes
   */
  private void setModes(SensorMode... modes) {
    this.modes = modes;
    modeList = Arrays.stream(modes).map(SensorMode::getName).collect(Collectors.toCollection(ArrayList::new));
    currentMode = 0;
  }

  /**
   * Returns all modes available from the sensor.
   *
   * @return List of modes available
   */
  @Override
  public ArrayList<String> getAvailableModes() {
    return modeList;
  }


  /**
   * Get a SensorMode associated with a mode index.
   *
   * <p><b>WARNING:</b> This function <b>does not</b>
   * switch the sensor to the correct mode. Unless the sensor is
   * switched to the correct mode, the reads from this SensorMode
   * will be invalid.
   * See {@link GenericMode#fetchSample(float[], int)}</p>
   */
  @Override
  public SensorMode getMode(int mode) {
    if (modeInvalid(mode)) {
      throw new IllegalArgumentException("Invalid mode " + mode);
    }
    return modes[mode];
  }


  /**
   * Get a SensorMode associated with a mode name.
   *
   * <p><b>WARNING:</b> This function <b>does not</b>
   * switch the sensor to the correct mode. Unless the sensor is
   * switched to the correct mode, the reads from this SensorMode
   * will be invalid.
   * See {@link GenericMode#fetchSample(float[], int)}</p>
   */
  @Override
  public SensorMode getMode(String modeName) {
    int index = getIndex(modeName);
    if (index != -1) {
      return modes[index];
    } else {
      throw new IllegalArgumentException("No such mode " + modeName);
    }
  }

  private boolean modeInvalid(int mode) {
    return modes == null || mode < 0 || mode >= modes.length;
  }

  private int getIndex(String modeName) {
    return getAvailableModes().indexOf(modeName);
  }

  @Override
  public int sampleSize() {
    return modes[currentMode].sampleSize();
  }

  /**
   * Set the current SensorMode index.
   *
   * <p><b>WARNING:</b> This function <b>does not</b>
   * switch the sensor to the correct mode. Unless the sensor is
   * switched to the correct mode, the reads from the"current" SensorMode
   * will be invalid.
   * See {@link GenericMode#fetchSample(float[], int)}</p>
   */
  @Override
  public void setCurrentMode(int mode) {
    if (modeInvalid(mode)) {
      throw new IllegalArgumentException("Invalid mode " + mode);
    } else {
      currentMode = mode;
    }
  }

  @Override
  public int getCurrentMode() {
    return currentMode;
  }

  /**
   * Set the current SensorMode name.
   *
   * <p><b>WARNING:</b> This function <b>does not</b>
   * switch the sensor to the correct mode. Unless the sensor is
   * switched to the correct mode, the reads from the"current" SensorMode
   * will be invalid.
   * See {@link GenericMode#fetchSample(float[], int)}</p>
   */
  @Override
  public void setCurrentMode(String modeName) {
    setCurrentMode(getIndex(modeName));
  }

  @Override
  public int getModeCount() {
    return modes.length;
  }
}
