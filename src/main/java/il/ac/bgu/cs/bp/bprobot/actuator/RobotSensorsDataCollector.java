package il.ac.bgu.cs.bp.bprobot.actuator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapperSerializeAdapter;
import il.ac.bgu.cs.bp.bprobot.util.communication.MQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import lejos.utility.Delay;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
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
    var message = "{}";
    while (!Thread.currentThread().isInterrupted()) {
      var map = update();
      var newMessage = toJson(map);
      if(!newMessage.equals(message)) {
        try {
          updateQueue(newMessage);
          message = newMessage;
        } catch (MqttException e) {
          throw new RuntimeException(e);
        }
      }
      Delay.msDelay(msDelay.get());
    }
  }

  private void updateQueue(String message) throws MqttException {
    comm.send(message, QueueNameEnum.Data);
  }

  private String toJson(Map<SensorWrapper<?>,float[]> data) {
    var builder = new GsonBuilder();
    Type swType = new TypeToken<SensorWrapper<?>>() {}.getType();
    Type mapType = new TypeToken<Map<SensorWrapper<?>,float[]>>() {}.getType();
    builder.registerTypeAdapter(swType, new SensorWrapperSerializeAdapter());
    return builder.create().toJson(data,mapType);
  }

  public void clear() {
    sensors.clear();
  }
}
