package il.ac.bgu.cs.bp.bprobot.util.robotdata;

import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import lejos.utility.Delay;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class RobotSensorsDataCollector implements Runnable {
  private final static Logger logger = Logger.getLogger("Robot Sensor Data");
  private final ConcurrentHashMap<SensorWrapper<?>, float[]> sensors = new ConcurrentHashMap<>();
  private final AtomicBoolean updated = new AtomicBoolean(false);
  private final AtomicLong msDelay = new AtomicLong(500);

  public RobotSensorsDataCollector() {

  }

  public RobotSensorsDataCollector(long msDelay) {
    this.msDelay.set(msDelay);
  }

  public void setMsDelay(long delay) {
    this.msDelay.set(delay);
  }

  public boolean isUpdated() {
    return updated.get();
  }

  public void addSensor(SensorWrapper<?> sensor) {
    sensors.put(sensor, new float[sensor.sampleSize()]);
  }

  private void update() {
    sensors.forEachEntry(1, e -> {
          if (e.getKey().sampleSize() != e.getValue().length)
            e.setValue(new float[e.getKey().sampleSize()]);
          e.getKey().fetchSample(e.getValue(), 0);
        }
    );
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      update();
      Delay.msDelay(msDelay.get());
    }
  }
}
