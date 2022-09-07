package il.ac.bgu.cs.bp.bprobot.actuator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import il.ac.bgu.cs.bp.bprobot.util.communication.IMQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.MQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DeviceActuator {

  private static CommandHandler commandHandler;
  private static IMQTTCommunication communicationHandler;

  public static void main(String[] args) throws MqttException {
    RobotSensorsDataCollector robotSensorsData = new RobotSensorsDataCollector();
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
    String action = obj.get("action").getAsString();
    JsonElement params = obj.getAsJsonObject("params");
    commandHandler.executeCommand(action, params);
  }
}
