package il.ac.bgu.cs.bp.bprobot.actuator;

import il.ac.bgu.cs.bp.bpjs.internal.Pair;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import lejos.utility.Delay;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RobotSensorsDataCollector implements Runnable {
  private final static Logger logger = Logger.getLogger("Robot Sensor Data");
  private final ConcurrentHashMap<SensorWrapper<?>, AtomicInteger> sensors = new ConcurrentHashMap<>();
  private final AtomicLong msDelay = new AtomicLong(500);
  private final Consumer<Map<SensorWrapper<?>, float[]>> updateSubscribers;

  public RobotSensorsDataCollector() {
    this(500);
  }

  public RobotSensorsDataCollector(long msDelay) {
    this.msDelay.set(msDelay);
    updateSubscribers = new Consumer<Map<SensorWrapper<?>, float[]>>() {
      @Override
      public void accept(Map<SensorWrapper<?>, float[]> sensorWrapperMap) {

      }
    }
  }

  public void setMsDelay(long delay) {
    this.msDelay.set(delay);
  }

  public void subscribe(SensorWrapper<?> sensor) {
    sensors.putIfAbsent(sensor, new AtomicInteger(0));
    sensors.get(sensor).incrementAndGet();
  }

  public void unsubscribe(SensorWrapper<?> sensor) {
    sensors.computeIfPresent(sensor, (s, p) -> p.decrementAndGet() == 0 ? null : p);
  }

  private Map<SensorWrapper<?>, float[]> update() {
    return sensors.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> {
      var res = new float[e.getKey().sampleSize()];
      e.getKey().fetchSample(res, 0);
      return res;
    }));
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      var map = update();
      Delay.msDelay(msDelay.get());
    }
  }

  public void clear() {
    sensors.clear();
  }
}
