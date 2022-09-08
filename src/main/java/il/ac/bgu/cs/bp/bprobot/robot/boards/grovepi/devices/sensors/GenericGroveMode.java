package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.sensors;

import lejos.hardware.sensor.SensorMode;

import java.util.function.Consumer;

public class GenericGroveMode implements SensorMode {
  private final String modeName;
  private final int sampleSize;
  private final float correctMin;
  private final float correctMax;
  private final float correctFactor;

  /**
   * Create new generic sensor handler.
   */
  public GenericGroveMode() {
    this(1, "DEFAULT");
  }

  /**
   * Create new generic sensor handler.
   *
   * @param sampleSize Number of returned samples.
   * @param modeName   Human-readable sensor mode name.
   */
  public GenericGroveMode(
      final int sampleSize,
      final String modeName) {
    this(sampleSize, modeName,
        Float.MIN_VALUE, Float.MAX_VALUE, 1.0f);
  }

  /**
   * Create new generic sensor handler.
   *
   * @param sampleSize    Number of returned samples.
   * @param modeName      Human-readable sensor mode name.
   * @param correctMin    Minimum value measured by the sensor. If the reading is lower, zero is returned.
   * @param correctMax    Maximum value measured by the sensor. If the reading is higher, positive infinity is returned.
   * @param correctFactor Scaling factor applied to the sensor reading.
   */
  public GenericGroveMode(
      final int sampleSize,
      final String modeName,
      final float correctMin,
      final float correctMax,
      final float correctFactor) {
    this.sampleSize = sampleSize;
    this.modeName = modeName;
    this.correctMin = correctMin;
    this.correctMax = correctMax;
    this.correctFactor = correctFactor;
  }

  @Override
  public String getName() {
    return modeName;
  }

  @Override
  public int sampleSize() {
    return sampleSize;
  }

  /**
   * Fetches a sample from the sensor.
   *
   * <p>Note: this function works properly only when
   * the sensor is already in the appropriate mode. Otherwise,
   * returned data will be invalid.</p>
   *
   * @param sample The array to store the sample in.
   * @param offset The elements of the sample are stored in the array starting at the offset position.
   */
  @Override
  public void fetchSample(float[] sample, int offset) {
    // for all values
    for (int n = 0; n < sampleSize; n++) {
      // apply correction
      sample[n] *= correctFactor;
      if (sample[n] < correctMin) {
        sample[n] = 0;
      } else if (sample[n] >= correctMax) {
        sample[n] = Float.POSITIVE_INFINITY;
      }
      // store
      sample[offset + n] = sample[n];
    }
  }
}
