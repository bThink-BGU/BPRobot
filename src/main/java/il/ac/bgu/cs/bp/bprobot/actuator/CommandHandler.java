package il.ac.bgu.cs.bp.bprobot.actuator;

import com.google.gson.*;
import ev3dev.sensors.BaseSensor;
import il.ac.bgu.cs.bp.bprobot.robot.Robot;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.util.communication.MQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import lejos.hardware.port.Port;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CommandHandler implements Runnable {
  private final RobotSensorsDataCollector dataCollector;
  private final MQTTCommunication comm;
  private Robot robot = null;

  // Uniform Interface for commands arriving from BPjs
  private final ICommand subscribe = this::subscribe;
  private final ICommand unsubscribe = this::unsubscribe;
  private final ICommand config = this::config;
  private final ICommand mockSensorReadings = this::mockSensorReadings;

  private final ICommand defaultCommand = this::ev3Command;

  // Thread for data collection from robot sensors
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> dataCollectionFuture;
  private final Map<String, ICommand> commandToMethod = Map.of(
      "subscribe", subscribe,
      "unsubscribe", unsubscribe,
      "config", config,
      "mockSensorReadings", mockSensorReadings
  );

  public CommandHandler() {
    comm = new MQTTCommunication();
    dataCollector = new RobotSensorsDataCollector(comm);
  }

  @Override
  public void run() {
    try {
      comm.connect();
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        comm.closeConnection();
      } catch (MqttException ignore) {
      }
      try {
        closeBoards();
      } catch (Exception ignore) {
      }
      System.out.println("CommandHandler: Connection Closed!");
    }));

    // Sending on Data and Free.
    // Listening on Commands and SOS.
    try {
      comm.consumeFromQueue(QueueNameEnum.Commands, this::onReceiveCallback);
      comm.consumeFromQueue(QueueNameEnum.SOS, this::onReceiveCallback);
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }

    dataCollector.run();
  }

  // Parse & execute command from message that arrived from BPjs
  public void executeCommand(String action, JsonElement params) throws IOException {
    ICommand commandToExecute = commandToMethod.getOrDefault(action, defaultCommand);
    commandToExecute.executeCommand(action, params);
  }

  void closeBoards() {
    if (robot != null)
      robot.close();
  }

  private void mockSensorReadings(String commandName, JsonElement params) {
    var obj = params.getAsJsonObject();
    var address = obj.getAsJsonPrimitive("address").getAsString();
    var delay = Optional.ofNullable(obj.get("delay")).orElse(new JsonPrimitive(0)).getAsLong();
    var valueJS = obj.getAsJsonArray("value");
    var value = new float[valueJS.size()];
    for (int i = 0; i < value.length; i++) {
      value[i] = valueJS.get(i).getAsFloat();
    }
    var board = robot.getBoard(address.substring(0, address.indexOf(".")));
    var port = board.getPort(address.substring(address.indexOf(".") + 1));
    var device = board.getDevice(port.getName()).device;

    var timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        Mockito.doAnswer(invocation -> {
          var values = invocation.<float[]>getArgument(0);
          System.arraycopy(value, 0, values, 0, values.length);
          return null;
        }).when((BaseSensor) device).fetchSample(Mockito.any(), Mockito.anyInt());
      }
    }, delay);
  }

  /**
   * Subscribe to new ports.
   * <p>
   * 1. Stop data collection from ports
   * 2. Add new ports to Robot Sensor Data Object.
   * 3. Restart Data Collection Thread.
   *
   * @param params json element from BPjs messages
   */
  void subscribe(String commandName, JsonElement params) {
    var address = params.getAsString();
    var board = robot.getBoard(address.substring(0, address.indexOf(".")));
    var port = board.getPort(address.substring(address.indexOf(".") + 1));
    subscribe((SensorWrapper<?>) board.getDevice(port.getName()));
  }

  private void subscribe(SensorWrapper<?> sensor) {
    dataCollector.subscribe(sensor);
    startExecutor();
  }

  /**
   * Unsubscribe from ports.
   * <p>
   * 1. Stop data collection from ports
   * 2. Remove ports from Robot Sensor Data Object.
   * 3. Restart Data Collection Thread.
   *
   * @param params from BPjs messages
   */
  void unsubscribe(String commandName, JsonElement params) {
    var address = params.getAsString();
    var board = robot.getBoard(address.substring(0, address.indexOf(".")));
    var port = board.getPort(address.substring(address.indexOf(".") + 1));
    unsubscribe((SensorWrapper<?>) board.getDevice(port.getName()));
  }

  private void unsubscribe(SensorWrapper<?> sensor) {
    dataCollector.unsubscribe(sensor);
  }

  /**
   * Build IBoards according to json data from BPjs Build event.
   *
   * @param params instructions on which IBoards to build.
   */
  void config(String action, JsonElement params) {
    try {
      robot = Robot.parse(params.getAsJsonObject());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    if (dataCollectionFuture != null) {
      dataCollectionFuture.cancel(true);
    }
    dataCollector.clear();
  }

  /**
   * Call IBoard's 'drive' method according to json data
   *
   * @param json info on boards, ports and values to call 'drive' on.
   */
  private void ev3Command(String commandName, JsonElement json) {
    if (robot == null) {
      throw new RuntimeException("Robot is not initialized");
    }
    for (var act : buildActivationMap(commandName, json.getAsJsonArray())) {
      var device = act.board.getDevice(act.port.getName());
      var methods = device.getClass().getMethods();
      Method method = null;
      Object[] params = null;
      for (int i = 0; i < methods.length && method == null; i++) {
        method = methods[i];
        if (method.getName().equals(act.function) && method.getParameterCount() == act.params.size()) {
          var paramsDef = method.getParameters();
          params = new Object[act.params.size()];
          for (int j = 0; j < params.length; j++) {
            var clazz = paramsDef[j].getType();
            if (clazz.isAssignableFrom(Byte.class)) {
              params[j] = act.params.get(i).getAsByte();
            } else if (clazz.isAssignableFrom(Short.class)) {
              params[j] = act.params.get(i).getAsShort();
            } else if (clazz.isAssignableFrom(Integer.class)) {
              params[j] = act.params.get(i).getAsInt();
            } else if (clazz.isAssignableFrom(Long.class)) {
              params[j] = act.params.get(i).getAsLong();
            } else if (clazz.isAssignableFrom(Double.class)) {
              params[j] = act.params.get(i).getAsDouble();
            } else if (clazz.isAssignableFrom(String.class)) {
              var s = act.params.get(i).getAsString();
              assert s.length() == 1;
              params[j] = s.charAt(0);
            } else if (clazz.isAssignableFrom(Boolean.class)) {
              params[j] = act.params.get(i).getAsBoolean();
            } else if (clazz.isAssignableFrom(String.class)) {
              params[j] = act.params.get(i).getAsString();
            } else {
              method = null;
              break;
            }
          }
        }
      }
      if (method == null) {
        throw new IllegalArgumentException("No such method " + act.function + " with params " + act.params.toString());
      }
      try {
        method.invoke(device, params);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Build Map of Board Types -> Board Index -> (Port, value)
   * This map is used to call 'drive' on each IBoard that is indexed on the result
   *
   * @param command the name of the function to execute
   * @param params  build map according to this json
   * @return Map with boards, their indexes, and the data to call 'drive' on.
   */
  private List<Act<?>> buildActivationMap(String command, JsonArray params) {
    List<Act<?>> result = new ArrayList<>();
    for (var param : params) {
      if (param.isJsonObject()) {
        var paramObj = param.getAsJsonObject();
        if (paramObj.has("address")) {
          var address = paramObj.getAsJsonPrimitive("address").getAsString();
          var board = robot.getBoard(address.substring(0, address.indexOf(".")));
          var port = board.getPort(address.substring(address.indexOf(".") + 1));
          var actionParams = new JsonArray();
          if (paramObj.has("params")) actionParams = paramObj.getAsJsonArray("params");
          result.add(new Act(board, port, command, actionParams));
        }
      }
    }
    return result;
  }

  private void startExecutor() {
    if (dataCollectionFuture != null) {
      dataCollectionFuture.cancel(true);
    }
    int commandTimeout = 100;
    dataCollectionFuture = executor.scheduleWithFixedDelay(dataCollector, 0L, commandTimeout, TimeUnit.MILLISECONDS);
  }

  private void onReceiveCallback(String topic, MqttMessage message) throws IOException {
    String msg = new String(message.getPayload(), StandardCharsets.UTF_8);
    JsonObject obj = JsonParser.parseString(msg).getAsJsonObject();
    String action = obj.get("action").getAsString();
    JsonElement params = obj.getAsJsonObject("params");
    executeCommand(action, params);
  }


  /**
   * Uniform Interface for BPjs Commands
   */
  @FunctionalInterface
  public interface ICommand {
    void executeCommand(String commandName, JsonElement params) throws IOException;
  }

  public static class Act<P extends Port> {
    final Board<P> board;
    final P port;
    final String function;
    final JsonArray params;

    private Act(Board<P> board, P port, String function, JsonArray params) {
      this.board = board;
      this.port = port;
      this.function = function;
      this.params = params;
    }
  }
}