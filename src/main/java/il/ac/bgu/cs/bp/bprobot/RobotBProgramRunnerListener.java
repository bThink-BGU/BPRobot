package il.ac.bgu.cs.bp.bprobot;

import com.google.common.base.Strings;
import com.google.gson.*;
import il.ac.bgu.cs.bp.bpjs.execution.listeners.BProgramRunnerListenerAdapter;
import il.ac.bgu.cs.bp.bpjs.model.BEvent;
import il.ac.bgu.cs.bp.bpjs.model.BProgram;
import il.ac.bgu.cs.bp.bprobot.util.communication.MQTTCommunication;
import il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RobotBProgramRunnerListener extends BProgramRunnerListenerAdapter {
  private final AtomicReference<String> sensorsData = new AtomicReference<>();
  private final MQTTCommunication comm;
  private final Map<String, QueueNameEnum> cmd2queue = Map.of(
      "SetSensorMode", QueueNameEnum.SOS,
      "SetActuatorData", QueueNameEnum.SOS,
      "subscribe", QueueNameEnum.SOS,
      "unsubscribe", QueueNameEnum.SOS,
      "build", QueueNameEnum.SOS
  );

  RobotBProgramRunnerListener(MQTTCommunication communication, BProgram bp) throws MqttException {
    comm = communication;
    comm.connect();
    comm.consumeFromQueue(QueueNameEnum.Data, (topic, message) ->
        sensorDataReceived(new String(message.getPayload(), StandardCharsets.UTF_8)));
    comm.consumeFromQueue(QueueNameEnum.Free, (topic, message) ->
        bp.enqueueExternalEvent(new BEvent("GetAlgorithmResult", new String(message.getPayload(), StandardCharsets.UTF_8))));
  }

  private void sensorDataReceived(String message) {
    sensorsData.set(message);
  }

  @Override
  public void eventSelected(BProgram bp, BEvent theEvent) {
    if (theEvent.name.equals("Command")) {
      if (Strings.isNullOrEmpty((String) theEvent.maybeData))
        throw new IllegalArgumentException("Command event does not include data");
      var json = JsonParser.parseString((String) theEvent.maybeData).getAsJsonObject();
      String action = json.getAsJsonPrimitive("action").getAsString();
      try {
        send((String)theEvent.maybeData, cmd2queue.getOrDefault(theEvent.name,QueueNameEnum.Commands));
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
