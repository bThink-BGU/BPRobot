package il.ac.bgu.cs.bp.bprobot.actuator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ev3dev.hardware.EV3DevPlatform;
import il.ac.bgu.cs.bp.bprobot.robot.Robot;
import il.ac.bgu.cs.bp.bprobot.robot.boards.DriveDataObject;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.util.robotdata.RobotSensorsData;
import lejos.hardware.port.Port;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler {
  RobotSensorsData robotSensorsData;

  Map<String, Board> robot;

  // Uniform Interface for commands arriving from BPjs
  private final ICommand subscribe = this::subscribe;
  private final ICommand unsubscribe = this::unsubscribe;
  private final ICommand build = this::build;
  private final ICommand defaultCommand = this::ev3Command;

  // Thread for data collection from robot sensors
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> dataCollectionFuture;

  private final Map<String, ICommand> commandToMethod = Stream.of(new Object[][]{{"\"Subscribe\"", subscribe}, {"\"Unsubscribe\"", unsubscribe}, {"\"Build\"", build}}).collect(Collectors.toMap(data -> (String) data[0], data -> (ICommand) data[1]));

  public CommandHandler(RobotSensorsData robotSensorsData) {
    this.robotSensorsData = robotSensorsData;
  }

  // Parse & execute command from message that arrived from BPjs
  public void executeCommand(String command, String dataJsonString) throws IOException {
    ICommand commandToExecute = commandToMethod.getOrDefault(command, defaultCommand);
    commandToExecute.executeCommand(dataJsonString);
  }

  void closeBoards() {
    if (robot == null) {
      return;
    }
    robot.values().forEach(Board::close);
  }

  /**
   * Subscribe to new ports.
   * <p>
   * 1. Stop data collection from ports
   * 2. Add new ports to Robot Sensor Data Object.
   * 3. Restart Data Collection Thread.
   *
   * @param json string from BPjs messages
   */
  void subscribe(String json) {
    robotSensorsData.addToBoardsMap(json);
    startExecutor();
  }

  /**
   * Unsubscribe from ports.
   * <p>
   * 1. Stop data collection from ports
   * 2. Remove ports from Robot Sensor Data Object.
   * 3. Restart Data Collection Thread.
   *
   * @param json string from BPjs messages
   */
  void unsubscribe(String json) {
    robotSensorsData.removeFromBoardsMap(json);
    startExecutor();
  }

  /**
   * Build IBoards according to json data from BPjs Build event.
   *
   * @param json instructions on which IBoards to build.
   */
  void build(String json) {
    try {
      robot = Robot.JsonToRobot(json);
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
  private void ev3Command(String json) throws InvocationTargetException, IllegalAccessException {
    if (robot == null) {
      throw new RuntimeException("Robot is not initialized");
    }
    for (var act : buildActivationMap(json)) {
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
      method.invoke(device, params);
    }
  }

  /**
   * Build Map of Board Types -> Board Index -> (Port, value)
   * This map is used to call 'drive' on each IBoard that is indexed on the result
   *
   * @param json build map according to this json
   * @return Map with boards, their indexes, and the data to call 'drive' on.
   */
  private List<Act> buildActivationMap(String json) {
    List<Act> result = new ArrayList<>();
    var element = JsonParser.parseString(json).getAsJsonObject();
    String action = element.getAsJsonPrimitive("action").getAsString();
    var params = element.getAsJsonArray("params");
    for (var param : params) {
      if (param.isJsonObject()) {
        var paramObj = param.getAsJsonObject();
        if (paramObj.has("address")) {
          var address = paramObj.getAsJsonPrimitive("address").getAsString();
          var board = robot.get(address.substring(0, address.indexOf(".")));
          var port = board.getPort(address.substring(address.indexOf(".") + 1));
          var actionParams = new JsonArray();
          if (paramObj.has("params")) actionParams = paramObj.getAsJsonArray("params");
          result.add(new Act(board, port, action, actionParams));
        }
      }
    }
    return result;
  }

  /**
   * Create Runnable, which build json with all the subscribed ports and their connected sensors values.
   * This json is then used to update the sensor values inside RobotSensorsData.
   */
  @SuppressWarnings("unchecked")
  private final Runnable dataCollector = () -> {

    try {
      RobotSensorsData robotSensorsDataCopy = robotSensorsData.deepCopy();

      JsonObject jsonBoards = new JsonObject();
      robotSensorsDataCopy.getBoardNames().forEach(boardString -> {
        JsonObject jsonIndexes = new JsonObject();
        EV3DevPlatform board = EV3DevPlatform.valueOf(boardString);
        robotSensorsDataCopy.getBoardIndexes(boardString).forEach(indexString -> {
          JsonObject jsonPorts = new JsonObject();
          int index = Integer.parseInt(indexString.substring(1));
          robotSensorsDataCopy.getPorts(boardString, indexString).forEach(portString -> {
            Port port = board.getPortType(portString);
            Double data = robot.get(board).get(index).getDoubleSensorData(port);
            if (data != null && !Double.isNaN(data)) {
              jsonPorts.addProperty(portString, data);
            }
          });
          jsonIndexes.add(indexString, jsonPorts);
        });
        jsonBoards.add(boardString, jsonIndexes);
      });
      robotSensorsData.updateBoardMapValues(jsonBoards.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  };

  private void startExecutor() {
    if (dataCollectionFuture != null) {
      dataCollectionFuture.cancel(true);
    }

    try {
      int commandTimeout = 100;
      dataCollectionFuture = executor.scheduleWithFixedDelay(dataCollector, 0L, commandTimeout, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Uniform Interface for BPjs Commands
   */
  @FunctionalInterface
  public interface ICommand {
    void executeCommand(String json) throws IOException;
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