package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.sensors;

import ev3dev.sensors.GenericMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class GroveSensorWrapper<T> extends SensorWrapper<T> {
  private ArrayList<String> modeList;
  protected int currentMode = 0;
  protected SensorMode[] modes;

  protected GroveSensorWrapper(String name, Port port, T device, GenericGroveMode... modes) {
    super(name, port, device);
    if (modes.length == 0)
      modes = new GenericGroveMode[]{new GenericGroveMode()};
    setModes(modes);
  }

  protected abstract void sample(float[] sample) throws Exception;

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

  public String getName() {
    return modes[currentMode].getName();
  }

  public int sampleSize() {
    return modes[currentMode].sampleSize();
  }

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
  public void fetchSample(float[] sample, int offset) {
    try {
      sample(sample);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    modes[currentMode].fetchSample(sample, offset);
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
  public void setCurrentMode(int mode) {
    if (modeInvalid(mode)) {
      throw new IllegalArgumentException("Invalid mode " + mode);
    } else {
      currentMode = mode;
    }
  }

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
  public void setCurrentMode(String modeName) {
    setCurrentMode(getIndex(modeName));
  }

  public int getModeCount() {
    return modes.length;
  }
}
