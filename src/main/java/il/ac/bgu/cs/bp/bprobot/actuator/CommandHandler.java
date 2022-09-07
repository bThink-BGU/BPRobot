package il.ac.bgu.cs.bp.bprobot.actuator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import il.ac.bgu.cs.bp.bprobot.robot.Robot;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import lejos.hardware.port.Port;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CommandHandler {
  RobotSensorsDataCollector robotSensorsData;
  private Robot robot = null;

  // Uniform Interface for commands arriving from BPjs
  private final ICommand subscribe = this::subscribe;
  private final ICommand unsubscribe = this::unsubscribe;
  private final ICommand build = this::build;
  private final ICommand defaultCommand = this::ev3Command;

  // Thread for data collection from robot sensors
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> dataCollectionFuture;
  private final Map<String, ICommand> commandToMethod = Map.of(
      "Subscribe", subscribe,
      "Unsubscribe", unsubscribe,
      "Build", build
  );

  public CommandHandler(RobotSensorsDataCollector robotSensorsData) {
    this.robotSensorsData = robotSensorsData;
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
    subscribe((SensorWrapper<?>) board.getDevice(port));
  }

  private void subscribe(SensorWrapper<?> sensor) {
    robotSensorsData.subscribe(sensor);
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
    unsubscribe((SensorWrapper<?>) board.getDevice(port));
  }

  private void unsubscribe(SensorWrapper<?> sensor) {
    robotSensorsData.unsubscribe(sensor);
  }

  /**
   * Build IBoards according to json data from BPjs Build event.
   *
   * @param params instructions on which IBoards to build.
   */
  void build(String action, JsonElement params) {
    try {
      robot = Robot.parse(params.getAsJsonObject());
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (dataCollectionFuture != null) {
      dataCollectionFuture.cancel(true);
    }
    robotSensorsData.clear();
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
      var device = act.board.getDevice(act.port);
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
  private List<Act> buildActivationMap(String command, JsonArray params) {
    List<Act> result = new ArrayList<>();
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
    dataCollectionFuture = executor.scheduleWithFixedDelay(robotSensorsData, 0L, commandTimeout, TimeUnit.MILLISECONDS);
  }


  /**
   * Uniform Interface for BPjs Commands
   */
  @FunctionalInterface
  public interface ICommand {
    void executeCommand(String commandName, JsonElement params) throws IOException;
  }

  public static class Act {
    final Board board;
    final Port port;
    final String function;
    final JsonArray params;

    private Act(Board board, Port port, String function, JsonArray params) {
      this.board = board;
      this.port = port;
      this.function = function;
      this.params = params;
    }
  }
}