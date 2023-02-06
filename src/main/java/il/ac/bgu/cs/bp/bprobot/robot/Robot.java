package il.ac.bgu.cs.bp.bprobot.robot;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.Ev3Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.RemoteEv3Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.communication.BluetoothCommunicator;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiBoard;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import lejos.hardware.port.Port;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Robot {
  private static final IParser ev3Parser = Robot::ev3Parser;
  private static final IParser grovePiParser = Robot::grovePiParser;
  private static final Map<String, IParser> parsers = Map.of(
      "EV3BRICK", ev3Parser,
      "GROVEPI", grovePiParser
  );
  private String mqttAddress = "localhost";
  private int mqttPort = 1833;
  private final Map<String, Board<?>> boards = new HashMap<>();
  private final Map<String, Device> nickname2DeviceWrapper = new HashMap<>();

  private Robot() {
  }

  public void close() {
    boards.values().forEach(Board::close);
  }

  public static Robot parse(JsonObject json) throws Exception {
    Robot robot = new Robot();
    if (json.has("mqtt")) {
      var mqtt = json.getAsJsonObject("mqtt");
      robot.mqttAddress = mqtt.get("address").getAsString();
      robot.mqttPort = mqtt.get("port").getAsInt();
    }
    var devices = json.getAsJsonArray("devices");
    for (int i = 0; i < devices.size(); i++) {
      var deviceJson = devices.get(i).getAsJsonObject();
      var isMocked = Optional.ofNullable(deviceJson.get("mock")).orElse(new JsonPrimitive(false)).getAsBoolean();
      var type = Optional.ofNullable(deviceJson.get("type")).orElseThrow(() -> new IllegalArgumentException("Device in build does not include a 'type' parameter")).getAsString();
      var address = Optional.ofNullable(deviceJson.get("address")).orElse(new JsonPrimitive("default")).getAsString();
      var parser = parsers.get(type);
      if (parser == null)
        throw new IllegalArgumentException("'" + type + "' is not a known type of device (Must be 'GROVEPI' or any ev3dev.hardware.EV3DevPlatform.*)");
      var name = Optional.ofNullable(deviceJson.get("name")).orElseThrow(() -> new IllegalArgumentException("Device in build does not include a 'name' parameter")).getAsString();
      if (robot.boards.containsKey(name))
        throw new IllegalArgumentException("There is more than one board with the name " + name);
      var ports = deviceJson.getAsJsonArray("ports");
      robot.boards.put(name, parser.executeParser(robot, name, address, ports, isMocked));
    }
    return robot;
  }

  public Board<?> getBoard(String name) {
    return boards.get(name);
  }

  private static Board ev3Parser(Robot robot, String boardName, String address, JsonArray ports, boolean isMocked) throws Exception {
    Board board;
    if(!isMocked && address.startsWith("bt:")) {
      board = new RemoteEv3Board(new BluetoothCommunicator(address.split(":")[1]), boardName);
    } else {
      board = new Ev3Board(boardName, isMocked);
    }
    addPorts(robot, board, ports);
    return board;
  }

  private static GrovePiBoard grovePiParser(Robot robot, String boardName, String address, JsonArray ports, boolean isMocked) throws Exception {
    var board = new GrovePiBoard(boardName, isMocked);
    addPorts(robot, board, ports);
    return board;
  }

  private static <P extends Port> void addPorts(Robot robot, Board<P> board, JsonArray ports) throws Exception {
    for (int i = 0; i < ports.size(); i++) {
      var portJson = ports.get(i).getAsJsonObject();
      var name = Optional.ofNullable(portJson.get("name")).orElse(new JsonPrimitive("")).getAsString();
      var address = portJson.get("address").getAsString();
      var type = portJson.get("type").getAsString();
      var mode = Optional.ofNullable(portJson.get("mode")).orElse(null);
      Device dev = null;
      if (mode == null) {
        dev = board.putDevice(board.getPort(address), name, type, (String) null);
      } else {
        if (mode.getAsJsonPrimitive().isNumber()) {
          dev = board.putDevice(board.getPort(address), name, type, mode.getAsInt());
        } else {
          dev = board.putDevice(board.getPort(address), name, type, mode.getAsString());
        }
      }

      if (!Strings.isNullOrEmpty(name)) {
        if (robot.nickname2DeviceWrapper.containsKey(name))
          throw new IllegalArgumentException("There is more than one device with the name " + name);
        robot.nickname2DeviceWrapper.put(name, dev);
      }
      String fullAddress = board.name + "." + address;
      if (robot.nickname2DeviceWrapper.containsKey(fullAddress))
        throw new IllegalArgumentException("There is more than one device at " + fullAddress);
      robot.nickname2DeviceWrapper.put(fullAddress, dev);
    }
  }

  public Collection<Board<?>> getBoards() {
    return boards.values();
  }

  public Device getDevice(String portNameOrNickname) {
    return nickname2DeviceWrapper.get(portNameOrNickname);
  }

  /**
   * Uniform Interface for Board Parser
   */
  @FunctionalInterface
  public interface IParser {
    @SuppressWarnings("rawtypes")
    Board executeParser(Robot robot, String name, String address, JsonArray ports, boolean isMocked) throws Exception;
  }
}