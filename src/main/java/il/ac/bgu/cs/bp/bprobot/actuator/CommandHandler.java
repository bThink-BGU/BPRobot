package il.ac.bgu.cs.bp.bprobot.actuator;

import il.ac.bgu.cs.bp.bprobot.robot.boards.DriveDataObject;
import il.ac.bgu.cs.bp.bprobot.robot.boards.IBoard;
import il.ac.bgu.cs.bp.bprobot.robot.enums.BoardTypeEnum;
import il.ac.bgu.cs.bp.bprobot.robot.enums.IPortEnums;
import il.ac.bgu.cs.bp.bprobot.robot.Robot;
import il.ac.bgu.cs.bp.bprobot.util.robotdata.RobotSensorsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler {
    RobotSensorsData robotSensorsData;

    Map<BoardTypeEnum, Map<Integer, IBoard>> robot;

    // Uniform Interface for commands arriving from BPjs
    private final ICommand subscribe = this::subscribe;
    private final ICommand unsubscribe = this::unsubscribe;
    private final ICommand build = this::build;
    private final ICommand drive = this::drive;
    private final ICommand rotate = this::rotate;
    private final ICommand setSensorMode = this::setSensorMode;
    private final ICommand setActuatorData = this::setActuatorData;


    // Thread for data collection from robot sensors
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> dataCollectionFuture;

    private final Map<String, ICommand> commandToMethod = Stream.of(new Object[][]{
            {"\"Subscribe\"", subscribe},
            {"\"Unsubscribe\"", unsubscribe},
            {"\"Build\"", build},
            {"\"Drive\"", drive},
            {"\"Rotate\"", rotate},
            {"\"SetSensorMode\"", setSensorMode},
            {"\"SetActuatorData\"", setActuatorData}

    }).collect(Collectors.toMap(data -> (String) data[0], data -> (ICommand) data[1]));

    public CommandHandler(RobotSensorsData robotSensorsData) {
        this.robotSensorsData = robotSensorsData;
    }

    // Parse & execute command from message that arrived from BPjs
    public void executeCommand(String command, String dataJsonString) throws IOException {

        ICommand commandToExecute = commandToMethod.get(command);
        commandToExecute.executeCommand(dataJsonString);
    }

    String executeAlgorithm(String jsonData) {
        if (robot == null) {
            return "";
        }
        String jsonResult = "";
        Gson gson = new Gson();
        Map<?, ?> element = gson.fromJson(jsonData, Map.class); // json String to Map

        for (Object key : element.keySet()) { // Iterate over board types
            BoardTypeEnum keyAsBoard = BoardTypeEnum.valueOf((String) key);

            Object indexesMap = element.get(key);

            @SuppressWarnings("unchecked")
            Map<String, Object> boardIndexes = (Map<String, Object>) indexesMap; // Map of board indexes to ports list

            for (Map.Entry<String, Object> indexesToParams : boardIndexes.entrySet()) {
                String index = indexesToParams.getKey();
                Object algorithmParams = indexesToParams.getValue();
                Integer boardIndexAsInt = Integer.parseInt(index);
                String jsonParams = gson.toJson(algorithmParams);
                jsonResult = robot.get(keyAsBoard).get(boardIndexAsInt).myAlgorithm(jsonParams);
            }
        }
        return jsonResult;
    }

    void closeBoards() {
        if (robot == null) {
            return;
        }

        robot.forEach((boardName, boards) -> boards.forEach((index, board) -> board.disconnect()));
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

//        Map<Integer, IBoard> ev3 = Map.of(1, new MockBoard(), 2, new MockBoard());
//        Map<Integer, IBoard> grovePi = Map.of(1, new MockBoard(), 2, new MockBoard());
//        robot = new HashMap<>();
//        robot.put(BoardTypeEnum.EV3, ev3);
//        robot.put(BoardTypeEnum.GrovePi, grovePi);

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
    private void drive(String json) {

        try {
            if (robot == null) {
                return;
            }
            Map<BoardTypeEnum, Map<Integer, Map.Entry<Map<IPortEnums, Double>, Double>>> activationMap = buildActivationMap(json);

            activationMap.forEach((boardName, indexesMap) -> {
                Map<Integer, IBoard> boardsMap = robot.get(boardName);

                activationMap.get(boardName).forEach((index, portsMap) -> {
                    IBoard board = boardsMap.get(index);

                    ArrayList<DriveDataObject> driveList = getDriveList(
                            activationMap.get(boardName).get(index).getKey(),
                            activationMap.get(boardName).get(index).getValue());

                    board.drive(driveList);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Call IBoard's 'rotate' method according to json data
     *
     * @param json info on boards, ports and values to call 'rotate' on.
     */
    private void rotate(String json) {
        try {
            if (robot == null) {
                return;
            }
            Map<BoardTypeEnum, Map<Integer, Map.Entry<Map<IPortEnums, Double>, Double>>> activationMap = buildActivationMap(json);

            activationMap.forEach((boardName, indexesMap) -> {
                Map<Integer, IBoard> boardsMap = robot.get(boardName);

                activationMap.get(boardName).forEach((index, portsMap) -> {
                    IBoard board = boardsMap.get(index);
                    ArrayList<DriveDataObject> driveList = getDriveList(
                            activationMap.get(boardName).get(index).getKey(),
                            activationMap.get(boardName).get(index).getValue());
                    board.rotate(driveList);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Call IBoard's 'setSensorData' method according to json data
     *
     * @param json info on boards, ports and values to call 'setSensorData' on.
     */
    private void setSensorMode(String json) {
        try {
            if (robot == null) {
                return;
            }
            Map<BoardTypeEnum, Map<Integer, Map.Entry<Map<IPortEnums, Double>, Double>>> activationMap = buildActivationMap(json);

            activationMap.forEach((boardName, indexesMap) -> {
                Map<Integer, IBoard> boardsMap = robot.get(boardName);

                activationMap.get(boardName).forEach((index, portsMap) -> {
                    IBoard board = boardsMap.get(index);
                    Map<IPortEnums, Double> sensorsDataMap = activationMap.get(boardName).get(index).getKey();
                    sensorsDataMap.forEach((port, sensorValue) -> board.setSensorMode(port, sensorValue.intValue()));
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActuatorData(String json) {
        try {
            if (robot == null) {
                return;
            }
            Map<BoardTypeEnum, Map<Integer, Map.Entry<Map<IPortEnums, Double>, Double>>> activationMap = buildActivationMap(json);

            activationMap.forEach((boardName, indexesMap) -> {
                Map<Integer, IBoard> boardsMap = robot.get(boardName);

                activationMap.get(boardName).forEach((index, portsMap) -> {
                    IBoard board = boardsMap.get(index);
                    Map<IPortEnums, Double> sensorsDataMap = activationMap.get(boardName).get(index).getKey();
                    sensorsDataMap.forEach((port, sensorValue) -> board.setActuatorData(port, sensorValue.intValue()));
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Build Map of Board Types -> IBoard Index -> Port and it's speed value.
     * This map is used to call 'drive' on each IBoard that is indexed on the result
     *
     * @param json build map according to this json
     * @return Map with boards, their indexes, and the data to call 'drive' on.
     */
    private Map<BoardTypeEnum, Map<Integer, Map.Entry<Map<IPortEnums, Double>, Double>>> buildActivationMap(String json) {
        Map<BoardTypeEnum, Map<Integer, Map.Entry<Map<IPortEnums, Double>, Double>>> result = new HashMap<>();
        Gson gson = new Gson();
        Map<?, ?> element = gson.fromJson(json, Map.class); // json String to Map

        for (Object key : element.keySet()) { // Iterate over board types
            BoardTypeEnum keyAsBoard = BoardTypeEnum.valueOf((String) key);
            result.put(keyAsBoard, new HashMap<>()); // Add board enum to map
            Object indexesMap = element.get(key);

            @SuppressWarnings("unchecked")
            Map<String, Object> valueMapped = (Map<String, Object>) indexesMap; // Map of boards to ports list

            // Check if board contains map of board indexes to map of ports and values
            // or if the map is immediately ports and values.
            // The latter case means treating it as ports that belong to the first board

            // We are going to check the mapping VALUE.
            // If its a map, then we have board indexing, else we have port and values.

            Optional<Object> anyValue = valueMapped.values().stream().findFirst();
            if (anyValue.isEmpty()) {
                continue;
            }

            // Check if board indexes exist
            if (anyValue.get() instanceof LinkedTreeMap) {
                // We know the rest of the structure from here
                @SuppressWarnings("unchecked")
                Map<String, Map<String, Double>> boardIndexes = (Map<String, Map<String, Double>>) indexesMap; // Map of boards to ports list

                boardIndexes.forEach((index, mapping) -> {
                    Integer boardIndexAsInt = Integer.parseInt(index);
                    //result.get(keyAsBoard).put(boardIndexAsInt, new HashMap<>());
//                    Map.Entry<Map<IPortEnums, Double>, Double>>
                    Double speed = getSpeed(mapping);
                    Map<IPortEnums, Double> portsAndValues = new HashMap<>();
                    mapping.forEach((port, value) ->
                            portsAndValues.put(keyAsBoard.getPortType(fixName(port)), value));

                    result.get(keyAsBoard).put(boardIndexAsInt, new AbstractMap.SimpleEntry<>(portsAndValues, speed));


                });

            } else if (anyValue.get() instanceof Double) {
                @SuppressWarnings("unchecked")
                Map<String, Double> boardPorts = (Map<String, Double>) indexesMap; // Map of boards to ports list
                Map<IPortEnums, Double> portsResult = new HashMap<>();


                Double speed = getSpeed(boardPorts);
                boardPorts.forEach((port, value) ->
                        portsResult.put(keyAsBoard.getPortType(fixName(port)), value));

                result.get(keyAsBoard).put(1, new AbstractMap.SimpleEntry<>(portsResult, speed));
            }
        }
        return result;
    }


    /**
     * Speed is hiding as a port in the mapping.
     * Retrieve speed from mapping and remove it.
     *
     * @param mapping to check for speed
     * @return speed
     */
    private Double getSpeed(Map<String, Double> mapping) {
        Double speed = null;
        if (mapping.containsKey("speed")) {
            speed = mapping.get("speed");
            mapping.remove("speed");
        }
        return speed;
    }


    private ArrayList<DriveDataObject> getDriveList(Map<IPortEnums, Double> speedMap, Double rotateSpeed) {
        ArrayList<DriveDataObject> driveList = new ArrayList<>();
        speedMap.forEach((port, speed) -> driveList.add(new DriveDataObject(port, speed, rotateSpeed == null ? 0 : rotateSpeed.intValue())));
        return driveList;
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
                BoardTypeEnum board = BoardTypeEnum.valueOf(boardString);
                robotSensorsDataCopy.getBoardIndexes(boardString).forEach(indexString -> {
                    JsonObject jsonPorts = new JsonObject();
                    int index = Integer.parseInt(indexString.substring(1));
                    robotSensorsDataCopy.getPorts(boardString, indexString).forEach(portString -> {
                        IPortEnums port = board.getPortType(portString);
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

    // Prepend '_' to port and board index names that start with a number
    private String fixName(String name) {
        char firstChar = name.charAt(0);
        return Character.isDigit(firstChar) ? "_" + name : name;
    }

    /**
     * Uniform Interface for BPjs Commands
     */
    @FunctionalInterface
    public interface ICommand {
        void executeCommand(String json) throws IOException;
    }
}