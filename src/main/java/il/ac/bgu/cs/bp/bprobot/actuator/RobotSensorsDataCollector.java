package il.ac.bgu.cs.bp.bprobot.actuator;

import com.google.gson.GsonBuilder;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.util.communication.MQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import lejos.utility.Delay;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RobotSensorsDataCollector implements Runnable {
  private final static Logger logger = Logger.getLogger("Robot Sensor Data");
  private final ConcurrentHashMap<SensorWrapper<?>, AtomicInteger> sensors = new ConcurrentHashMap<>();
  private final AtomicLong msDelay = new AtomicLong(500);
  private final MQTTCommunication comm;

  public RobotSensorsDataCollector(MQTTCommunication comm) {
    this(comm, 500);
  }

  public RobotSensorsDataCollector(MQTTCommunication comm, long msDelay) {
    this.msDelay.set(msDelay);
    this.comm = comm;
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
    try {
      while (!Thread.currentThread().isInterrupted()) {
        var map = update();
        var message = toJson(map);
        updateQueue(message);
        Delay.msDelay(msDelay.get());
      }
    } catch (MqttException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    System.out.println("Robot Sensor Data Collector stopped");
  }

  private void updateQueue(String message) throws MqttException {
    comm.send(message, QueueNameEnum.Data);
  }

  private String toJson(Map<SensorWrapper<?>, float[]> data) {
    var builder = new GsonBuilder();
    var map = data.entrySet().stream()
        .collect(Collectors.toUnmodifiableMap(
            e -> e.getKey().board + "." + e.getKey().port.getName(),
            e -> Map.of(
                "port", e.getKey().port.getName(),
                "board", e.getKey().board,
                "name", e.getKey().name,
                "mode", e.getKey().getCurrentMode(),
                "value", e.getValue()
            )));
    return builder.create().toJson(map);
  }

  public void clear() {
    sensors.clear();
  }
}
