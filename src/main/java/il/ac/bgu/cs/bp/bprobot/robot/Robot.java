package il.ac.bgu.cs.bp.bprobot.robot;

import com.github.yafna.raspberry.grovepi.GroveDigitalOut;
import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.*;
import com.github.yafna.raspberry.grovepi.pi4j.GrovePi4J;
import com.google.gson.Gson;
import ev3dev.hardware.EV3DevDevice;
import ev3dev.hardware.EV3DevPlatform;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Ev3Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiBoard;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.get.*;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.set.BuzzerWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.set.LedWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.set.RelayWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Robot {
    private static final IParser ev3Parser = Robot::ev3Parser;
    private static final IParser grovePiParser = Robot::grovePiParser;
    private static final Map<String, IParser> boardToParser = Stream.of(new Object[][]{
            {"EV3", ev3Parser},
            {"GrovePi", grovePiParser}

    }).collect(Collectors.toMap(data -> (String) data[0], data -> (IParser) data[1]));
//    public static void main(String[] args) throws IOException, InterruptedException {
//
////        InputStream inputStream = new FileInputStream("./classes/Robot.json");
////        byte[] data = inputStream.readAllBytes();
////        String jsonString = new String(data);
////        Map<BoardTypeEnum, Map<Integer, IBoard>> boards = JsonToRobot(jsonString);
////
////        Boards.Ev3Board ev3B = (Boards.Ev3Board) boards.get(BoardTypeEnum.EV3).get(1);
////        Boards.GrovePiBoard grovePi = (Boards.GrovePiBoard) boards.get(BoardTypeEnum.GrovePi).get(1);
////        while (true){
////            ev3B.getDoubleSensorData(Ev3SensorPort._1);
////            System.out.print(" ; ");
////            ev3B.getDoubleSensorData(Ev3SensorPort._4);
////            System.out.println();
////        }
//    }

    /**
     * reads a json file with the existing boards and their sensors.
     *
     * @param jsonString of the json file
     * @return HashMap of all the boards from the json
     */
    public static Map<EV3DevPlatform, Map<Integer, Board>> JsonToRobot(String jsonString) {

        Map<EV3DevPlatform, Map<Integer, Board>> retMap = new HashMap<>();
        Gson gson = new Gson();
        Map<?, ?> element = gson.fromJson(jsonString, Map.class); // json String to Map

        for (Object key : element.keySet()) { // Iterate over board types

            String boardName = (String) key;
            Object value = element.get(key);

            @SuppressWarnings("unchecked")
            ArrayList<Map<String, String>> boardConfigsList = (ArrayList<Map<String, String>>) value;
            Map<Integer, Board> boardsMap = new HashMap<>();

            for (int i = 0; i < boardConfigsList.size(); i++) {
                Map<String, String> config = boardConfigsList.get(i);
                try {
                    @SuppressWarnings("rawtypes")
                    Board configsBoard = boardToParser.get(boardName).executeParser(config);
                    boardsMap.put(i + 1, configsBoard);
                } catch (IOException e) {
                    System.out.println("Failed to initiate board " + boardName + " configs number " + (i + 1));
                }
            } // Go over each config and create a board from it.

            retMap.put(EV3DevPlatform.valueOf(boardName), boardsMap); // Add board type to map
        }
        return retMap;
    }

    private static Ev3Board ev3Parser(Map<String, String> config) {
        return new Ev3Board();
    }

    private static GrovePiBoard grovePiParser(Map<String, String> config) throws IOException {
        GrovePi grovePi = new GrovePi4J();

        Map<String, IGroveDeviceSensorWrapper> sensorSetMap = new HashMap<>();
        Map<String, IGroveDeviceSensorWrapper> sensorGetMap = new HashMap<>();

        for (Map.Entry<String, String> sensorData : config.entrySet()) {
            int portNumber = Integer.parseInt(sensorData.getKey().substring(1));
            switch (sensorData.getValue()) {
                case "Led":
                    sensorSetMap.put(sensorData.getKey(), new LedWrapper(new GroveLed(grovePi, portNumber)));
                    continue;

                case "Ultrasonic":
                    sensorGetMap.put(sensorData.getKey(), new UltrasonicWrapper(new GroveUltrasonicRanger(grovePi, portNumber)));
                    continue;

                case "Sound":
                    sensorGetMap.put(sensorData.getKey(), new SoundWrapper(new GroveSoundSensor(grovePi, portNumber)));
                    continue;

                case "Button":
                    sensorGetMap.put(sensorData.getKey(), new ButtonWrapper(grovePi.getDigitalIn(portNumber)));
                    continue;

                case "Rotary":
                    sensorGetMap.put(sensorData.getKey(), new RotaryWrapper(new GroveRotarySensor(grovePi, portNumber)));
                    continue;

                case "Relay":
                    sensorSetMap.put(sensorData.getKey(), new RelayWrapper(new GroveRelay(grovePi, portNumber)));
                    continue;

                case "Light":
                    sensorGetMap.put(sensorData.getKey(), new LightWrapper(new GroveLightSensor(grovePi, portNumber)));
                    continue;

                case "Buzzer":
                    sensorSetMap.put(sensorData.getKey(), new BuzzerWrapper(new GroveDigitalOut(grovePi, portNumber)));
                    continue;

            }

            if (sensorData.getValue().length() == "Temperature ".length() &&
                    sensorData.getValue().startsWith("Temperature ")) {

                String tempType = sensorData.getValue().substring("Temperature ".length());
                GroveTemperatureAndHumiditySensor.Type dhtType =
                        GroveTemperatureAndHumiditySensor.Type.valueOf(tempType);

                sensorGetMap.put(sensorData.getKey(),
                        new TemperatureWrapper(new GroveTemperatureAndHumiditySensor(grovePi, portNumber, dhtType)));
            }
        }
        return new GrovePiBoard(sensorGetMap, sensorSetMap);
    }

    /**
     * Uniform Interface for Board Parser
     */
    @FunctionalInterface
    public interface IParser {
        @SuppressWarnings("rawtypes")
        Board executeParser(Map<String, String> config) throws IOException;
    }
}