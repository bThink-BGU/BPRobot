package il.ac.bgu.cs.bp.bprobot.actuator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import il.ac.bgu.cs.bp.bprobot.util.communication.IMQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.MQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import il.ac.bgu.cs.bp.bprobot.util.robotdata.RobotSensorsData;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DeviceActuator {

  private static CommandHandler commandHandler;
  private static IMQTTCommunication communicationHandler;

  public static void main(String[] args) throws MqttException {
    RobotSensorsData robotSensorsData = new RobotSensorsData();
    commandHandler = new CommandHandler(robotSensorsData);
    communicationHandler = new MQTTCommunication();
    communicationHandler.connect();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        communicationHandler.closeConnection();
        commandHandler.closeBoards();
      } catch (MqttException ignore) {
      }
      System.out.println("Connection Closed!");

    }));

//        String json = "{\"EV3\": {1: {}, 2 : {\"B\": 32, \"C\": 31}}}";
//        commandHandler.getCommand("\"Drive\"").executeCommand(json);

    // Sending on Data and Free.
    // Listening on Commands and SOS.
    communicationHandler.consumeFromQueue(QueueNameEnum.Commands, DeviceActuator::onReceiveCallback);
    communicationHandler.consumeFromQueue(QueueNameEnum.SOS, DeviceActuator::onReceiveCallback);

    //noinspection InfiniteLoopStatement
    while (true) {
      if (robotSensorsData.isUpdated()) {
        String json = robotSensorsData.toJson();
        communicationHandler.send(json, QueueNameEnum.Data);

      }
    }
  }

  private static void onReceiveCallback(String topic, MqttMessage message) throws IOException {
    String msg = new String(message.getPayload(), StandardCharsets.UTF_8);
    JsonObject obj = JsonParser.parseString(msg).getAsJsonObject();
    String command = String.valueOf(obj.get("Command"));
    String dataJsonString = String.valueOf(obj.get("Data"));
    commandHandler.executeCommand(command, dataJsonString);
  }
}
