package il.ac.bgu.cs.bp.bprobot.robot;

import com.github.yafna.raspberry.grovepi.GroveDigitalOut;
import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.*;
import com.github.yafna.raspberry.grovepi.pi4j.GrovePi4J;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Ev3Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiBoard;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.GroveDeviceWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.sensors.*;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.actuators.BuzzerWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.actuators.LedWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.actuators.RelayWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Robot {
    private static final IParser ev3Parser = Robot::ev3Parser;
    private static final IParser grovePiParser = Robot::grovePiParser;
    private static final Map<String, IParser> parsers = Map.of(
        "EV3BRICK", ev3Parser,
        "GROVEPI", grovePiParser
    );
    private String mqttAddress = "localhost";
    private int mqttPort = 1833;
    private Map<String, Board> boards = new HashMap<>();

    public void JsonToRobot(JsonObject json) {
        if(json.has("mqtt")) {
            var mqtt = json.getAsJsonObject("mqtt");
            mqttAddress = mqtt.get("address").getAsString();
            mqttPort = mqtt.get("port").getAsInt();
        }
        var devices = json.getAsJsonArray("devices");
        for (int i = 0; i < devices.size(); i++) {
            var deviceJson = devices.get(i).getAsJsonObject();
            var type = deviceJson.get("type").getAsString();
            var name = deviceJson.get("name").getAsString();
            var ports = deviceJson.getAsJsonArray("ports");
            boards.put(name, parsers.get(type).executeParser(name, ports));
        }
    }

    private static Ev3Board ev3Parser(String name, JsonArray ports) {
        return new Ev3Board();
    }

    private static GrovePiBoard grovePiParser(String boardName, JsonArray ports) throws IOException {
        GrovePi grovePi = new GrovePi4J();
        Map<GrovePiPort, GroveDeviceWrapper> deviceWrappers = new HashMap<>();

        for (int i = 0; i < ports.size(); i++) {
            var port = ports.get(i).getAsJsonObject();
            var address = port.get("address").getAsString();
            var name = port.get("name").getAsString();
            var type = port.get("type").getAsString();
            var subType = port.get("subType").getAsString();
        }
            int portNumber = Integer.parseInt(sensorData.getKey().substring(1));
            switch (sensorData.getValue()) {
                case "Led":
                    deviceWrappers.put(sensorData.getKey(), new LedWrapper(new GroveLed(grovePi, portNumber)));
                    continue;

                case "Ultrasonic":
                    deviceWrappers.put(sensorData.getKey(), new UltrasonicWrapper(new GroveUltrasonicRanger(grovePi, portNumber)));
                    continue;

                case "Sound":
                    deviceWrappers.put(sensorData.getKey(), new SoundWrapper(new GroveSoundSensor(grovePi, portNumber)));
                    continue;

                case "Button":
                    deviceWrappers.put(sensorData.getKey(), new ButtonWrapper(grovePi.getDigitalIn(portNumber)));
                    continue;

                case "Rotary":
                    deviceWrappers.put(sensorData.getKey(), new RotaryWrapper(new GroveRotarySensor(grovePi, portNumber)));
                    continue;

                case "Relay":
                    deviceWrappers.put(sensorData.getKey(), new RelayWrapper(new GroveRelay(grovePi, portNumber)));
                    continue;

                case "Light":
                    deviceWrappers.put(sensorData.getKey(), new LightWrapper(new GroveLightSensor(grovePi, portNumber)));
                    continue;

                case "Buzzer":
                    deviceWrappers.put(sensorData.getKey(), new BuzzerWrapper(new GroveDigitalOut(grovePi, portNumber)));
                    continue;

            }

            if (sensorData.getValue().length() == "Temperature ".length() &&
                    sensorData.getValue().startsWith("Temperature ")) {

                String tempType = sensorData.getValue().substring("Temperature ".length());
                GroveTemperatureAndHumiditySensor.Type dhtType =
                        GroveTemperatureAndHumiditySensor.Type.valueOf(tempType);

                deviceWrappers.put(sensorData.getKey(),
                        new TemperatureWrapper(new GroveTemperatureAndHumiditySensor(grovePi, portNumber, dhtType)));
            }
        }
        return new GrovePiBoard(deviceWrappers);
    }

    /**
     * Uniform Interface for Board Parser
     */
    @FunctionalInterface
    public interface IParser {
        @SuppressWarnings("rawtypes")
        Board executeParser(String name, JsonArray ports) throws IOException;
    }
}