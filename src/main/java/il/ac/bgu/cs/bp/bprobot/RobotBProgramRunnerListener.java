package il.ac.bgu.cs.bp.bprobot;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import il.ac.bgu.cs.bp.bpjs.execution.listeners.BProgramRunnerListenerAdapter;
import il.ac.bgu.cs.bp.bpjs.model.BEvent;
import il.ac.bgu.cs.bp.bpjs.model.BProgram;
import il.ac.bgu.cs.bp.bprobot.actuator.CommandHandler;
import il.ac.bgu.cs.bp.bprobot.util.communication.IMQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import il.ac.bgu.cs.bp.bprobot.util.robotdata.RobotSensorsData;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RobotBProgramRunnerListener extends BProgramRunnerListenerAdapter {
  private final CommandHandler commandHandler;
  private final RobotSensorsData robotData = new RobotSensorsData();
  private final IMQTTCommunication com;
  private final ICommand subscribe = this::subscribe;
  private final ICommand unsubscribe = this::unsubscribe;
  private final ICommand build = this::build;
  private final ICommand drive = this::drive;
  private final ICommand rotate = this::rotate;
  private final ICommand setSensorMode = this::setSensorMode;
  private final ICommand setActuatorData = this::setActuatorData;
  private final ICommand myAlgorithm = this::myAlgorithm;
  private final ICommand test = this::test;
  private final Map<String, ICommand> commandToMethod = Stream.of(new Object[][]{
      {"Subscribe", subscribe},
      {"Unsubscribe", unsubscribe},
      {"Build", build},
      {"Drive", drive},
      {"Rotate", rotate},
      {"SetSensorMode", setSensorMode},
      {"SetActuatorData", setActuatorData},
      {"MyAlgorithm", myAlgorithm},
      {"Test", test}
  }).collect(Collectors.toMap(data -> (String) data[0], data -> (ICommand) data[1]));

  RobotBProgramRunnerListener(IMQTTCommunication communication, BProgram bp) throws MqttException {
    com = communication;
    commandHandler = new CommandHandler(robotData);
    com.connect();
    com.consumeFromQueue(QueueNameEnum.Data, (topic, message) ->
        robotData.updateBoardMapValues(new String(message.getPayload(), StandardCharsets.UTF_8)));
    com.consumeFromQueue(QueueNameEnum.Free, (topic, message) ->
        bp.enqueueExternalEvent(new BEvent("GetAlgorithmResult", new String(message.getPayload(), StandardCharsets.UTF_8))));
    com.consumeFromQueue(QueueNameEnum.Commands, (topic, message) -> onReceiveCommandCallback(commandHandler, topic, message));
    com.consumeFromQueue(QueueNameEnum.SOS, (topic, message) -> onReceiveCommandCallback(commandHandler, topic, message));
  }

  private static void onReceiveCommandCallback(CommandHandler commandHandler, String topic, MqttMessage message) throws IOException {
    String msg = new String(message.getPayload(), StandardCharsets.UTF_8);
    JsonObject obj = new JsonParser().parse(msg).getAsJsonObject();
    String command = String.valueOf(obj.get("Command"));
    String dataJsonString = String.valueOf(obj.get("Data"));
    commandHandler.executeCommand(command, dataJsonString);
  }

  @Override
  public void eventSelected(BProgram bp, BEvent theEvent) {
    if (commandToMethod.containsKey(theEvent.name)) {
      try {
        commandToMethod.get(theEvent.name).executeCommand(bp, theEvent);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  @Override
  public void superstepDone(BProgram bp) {
    String json = robotData.toJson();
//        System.out.println(json);
    bp.enqueueExternalEvent(new BEvent("GetSensorsData", json));
  }


  private String eventDataToJson(BEvent theEvent, String command) {
    String jsonString = parseObjectToJsonString(theEvent.maybeData);
    switch (command) {
      case "Build":
        try {
          robotData.buildNicknameMaps(jsonString);
        } catch (Exception e) {
          e.printStackTrace();
        }
        jsonString = cleanNicknames(jsonString);
        break;

      case "SetSensorMode":
      case "SetActuatorData":
      case "Drive":
      case "Rotate":
      case "Subscribe":
      case "Unsubscribe":
        jsonString = robotData.replaceNicksInJson(jsonString);
        break;
    }

    JsonElement jsonElement = new JsonParser().parse(jsonString);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("Command", command);
    jsonObject.add("Data", jsonElement);
    return jsonObject.toString();
  }

  private String parseObjectToJsonString(Object data) {
    return new Gson().toJson(data, Map.class);
  }

  private void test(BProgram bp, BEvent theEvent) {
    System.out.println("Test Completed!");
  }

  private void subscribe(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "Subscribe");
    String jsonString = parseObjectToJsonString(theEvent.maybeData);

    send(message, QueueNameEnum.SOS);
    robotData.addToBoardsMap(jsonString);
  }

  private void unsubscribe(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "Unsubscribe");
    String jsonString = parseObjectToJsonString(theEvent.maybeData);

    send(message, QueueNameEnum.SOS);
    robotData.removeFromBoardsMap(jsonString);
  }

  private void build(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "Build");
    send(message, QueueNameEnum.SOS);
  }

  private void drive(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "Drive");
    send(message, QueueNameEnum.Commands);
  }

  private void rotate(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "Rotate");
    send(message, QueueNameEnum.Commands);
  }

  private void setSensorMode(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "SetSensorMode");
    send(message, QueueNameEnum.SOS);
  }

  private void setActuatorData(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "SetActuatorData");
    send(message, QueueNameEnum.SOS);
  }

  private void myAlgorithm(BProgram bp, BEvent theEvent) {
    String message = eventDataToJson(theEvent, "MyAlgorithm");
    send(message, QueueNameEnum.SOS);
  }

  private void send(String message, QueueNameEnum queue) {
    try {
      com.send(message, queue);
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  private String cleanNicknames(String jsonString) {
    Gson gson = new Gson();
    Map<?, ?> element = gson.fromJson(jsonString, Map.class); // json String to Map
    for (Object boardNameKey : element.keySet()) { // Iterate over board types
      @SuppressWarnings("unchecked")
      ArrayList<Map<String, ?>> boardsDataList =
          (ArrayList<Map<String, ?>>) element.get(boardNameKey);

      for (int i = 0; i < boardsDataList.size(); i++) {
        Map<String, ?> portDataMap = boardsDataList.get(i);
        portDataMap.remove("Name");
        Map<String, String> newPortsValues = new HashMap<>();
        for (Map.Entry<String, ?> ports : portDataMap.entrySet()) {
          if (ports.getValue() instanceof LinkedTreeMap) { // Check if port value is actually a map with nickname
            @SuppressWarnings("unchecked")
            Map<String, String> valueMap = (Map<String, String>) ports.getValue();
            if (valueMap.containsKey("Device")) {
              String nickname = valueMap.get("Device");
              newPortsValues.put(ports.getKey(), nickname);
            }
          } else {
            newPortsValues.put(ports.getKey(), (String) ports.getValue());
          }
        }
        boardsDataList.set(i, newPortsValues);
      }
    }
    return new GsonBuilder().create().toJson(element);
  }

  /**
   * Uniform Interface for BPjs Commands
   */
  @FunctionalInterface
  private interface ICommand {
    void executeCommand(BProgram bp, BEvent theEvent) throws IOException;
  }
}
