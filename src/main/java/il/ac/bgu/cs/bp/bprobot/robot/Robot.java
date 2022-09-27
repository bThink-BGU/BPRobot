package il.ac.bgu.cs.bp.bprobot.robot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.Ev3Board;
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

    private Robot() {}

    public void close() {
        boards.values().forEach(Board::close);
    }
    public static Robot parse(JsonObject json) throws Exception {
        Robot robot = new Robot();
        if(json.has("mqtt")) {
            var mqtt = json.getAsJsonObject("mqtt");
            robot.mqttAddress = mqtt.get("address").getAsString();
            robot.mqttPort = mqtt.get("port").getAsInt();
        }
        var devices = json.getAsJsonArray("devices");
        for (int i = 0; i < devices.size(); i++) {
            var deviceJson = devices.get(i).getAsJsonObject();
            var isMocked = Optional.ofNullable(deviceJson.get("mock")).orElse(new JsonPrimitive(false)).getAsBoolean();
            var type = Optional.ofNullable(deviceJson.get("type")).orElseThrow(() -> new IllegalArgumentException("Device in build does not include a 'type' parameter")).getAsString();
            var parser = parsers.get(type);
            if(parser==null) throw new IllegalArgumentException("'"+type+"' is not a known type of device (Must be 'GROVEPI' or any ev3dev.hardware.EV3DevPlatform.*)");
            var name = Optional.ofNullable(deviceJson.get("name")).orElseThrow(() -> new IllegalArgumentException("Device in build does not include a 'name' parameter")).getAsString();
            if(robot.boards.containsKey(name)) throw new IllegalArgumentException("There is more than one board with the name "+name);
            var ports = deviceJson.getAsJsonArray("ports");
            robot.boards.put(name, parser.executeParser(name, ports, isMocked));
        }
        return robot;
    }

    public Board<?> getBoard(String name) {
        return boards.get(name);
    }

    private static Ev3Board ev3Parser(String boardName, JsonArray ports, boolean isMocked) throws Exception {
        var board = new Ev3Board(isMocked);
        addPorts(board, ports);
        return board;
    }

    private static GrovePiBoard grovePiParser(String boardName, JsonArray ports, boolean isMocked) throws Exception {
        var board = new GrovePiBoard(isMocked);
        addPorts(board, ports);
        return board;
    }

    private static <P extends Port> void addPorts(Board<P> board, JsonArray ports) throws Exception {
        for (int i = 0; i < ports.size(); i++) {
            var portJson = ports.get(i).getAsJsonObject();
            var name = Optional.ofNullable(portJson.get("name")).orElse(new JsonPrimitive("")).getAsString();
            var address = portJson.get("address").getAsString();
            var type = portJson.get("type").getAsString();
            var mode = Optional.ofNullable(portJson.get("mode")).map(JsonElement::getAsInt).orElse(null);
            board.putDevice(board.getPort(address), name, type, mode);
        }
    }

    public Collection<Board<?>> getBoards() {
        return boards.values();
    }

    /**
     * Uniform Interface for Board Parser
     */
    @FunctionalInterface
    public interface IParser {
        @SuppressWarnings("rawtypes")
        Board executeParser(String name, JsonArray ports, boolean isMocked) throws Exception;
    }
}