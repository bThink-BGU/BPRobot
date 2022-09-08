package il.ac.bgu.cs.bp.bprobot;

import com.google.common.base.Strings;
import com.google.gson.*;
import il.ac.bgu.cs.bp.bpjs.execution.listeners.BProgramRunnerListenerAdapter;
import il.ac.bgu.cs.bp.bpjs.internal.ScriptableUtils;
import il.ac.bgu.cs.bp.bpjs.model.BEvent;
import il.ac.bgu.cs.bp.bpjs.model.BProgram;
import il.ac.bgu.cs.bp.bprobot.util.communication.MQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.mozilla.javascript.NativeObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RobotBProgramRunnerListener extends BProgramRunnerListenerAdapter {
  private final AtomicReference<String> sensorsData = new AtomicReference<>();
  private MQTTCommunication comm;
  private final Map<String, QueueNameEnum> cmd2queue = Map.of(
      "SetSensorMode", QueueNameEnum.SOS,
      "SetActuatorData", QueueNameEnum.SOS,
      "subscribe", QueueNameEnum.SOS,
      "unsubscribe", QueueNameEnum.SOS,
      "config", QueueNameEnum.SOS
  );

  private void sensorDataReceived(String message) {
    sensorsData.set(message);
  }

  @Override
  public void eventSelected(BProgram bp, BEvent theEvent) {
    if (theEvent.name.equals("Command")) {
      var message = (String) theEvent.maybeData;
      if (Strings.isNullOrEmpty(message))
        throw new IllegalArgumentException("Command event does not include legal data");
      var json = JsonParser.parseString(message).getAsJsonObject();
      String action = json.get("action").getAsString();
      try {
        if (action.equals("config")) {
          comm = new MQTTCommunication();
          comm.connect();
          Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
              comm.closeConnection();
              System.out.println("Connection Closed!");
            } catch (MqttException e) {
              e.printStackTrace();
            }
          }));
          comm.consumeFromQueue(QueueNameEnum.Data, (topic, m) ->
              sensorDataReceived(new String(m.getPayload(), StandardCharsets.UTF_8)));
          comm.consumeFromQueue(QueueNameEnum.Free, (topic, m) ->
              bp.enqueueExternalEvent(new BEvent("GetAlgorithmResult", new String(m.getPayload(), StandardCharsets.UTF_8))));
        }
        send(message, cmd2queue.getOrDefault(action, QueueNameEnum.Commands));
      } catch (MqttException e) {
        throw new RuntimeException(e);
      }
    }
  }


  @Override
  public void superstepDone(BProgram bp) {
    String json = sensorsData.get();
    if (json != null)
      bp.enqueueExternalEvent(new BEvent("SensorsData", json));
  }

  private void send(String message, QueueNameEnum queue) throws MqttException {
    comm.send(message, queue);
  }
}
