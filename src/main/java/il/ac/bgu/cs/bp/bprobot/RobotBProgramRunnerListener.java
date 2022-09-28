package il.ac.bgu.cs.bp.bprobot;

import il.ac.bgu.cs.bp.bpjs.BPjs;
import il.ac.bgu.cs.bp.bpjs.execution.listeners.BProgramRunnerListenerAdapter;
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
      "mockSensorReadings", QueueNameEnum.SOS,
      "mockSensorSampleSize", QueueNameEnum.SOS,
      "subscribe", QueueNameEnum.SOS,
      "unsubscribe", QueueNameEnum.SOS,
      "config", QueueNameEnum.SOS
  );

  private void sensorDataReceived(BProgram bp, String message) {
    sensorsData.set(message);
    bp.enqueueExternalEvent(new BEvent("SensorsData", sensorsData));
  }

  @Override
  public void eventSelected(BProgram bp, BEvent theEvent) {
    if (theEvent.name.equals("Command")) {
      var cmd = (NativeObject) theEvent.maybeData;
      AtomicReference<String> message = new AtomicReference<>();
      BPjs.withContext(context -> {
        String name = "temp" + System.currentTimeMillis();
        bp.putInGlobalScope(name, cmd);
        message.set((String) context.evaluateString(bp.getGlobalScope(), "JSON.stringify(" + name + ")", "jsonify", 0, null));
        bp.getGlobalScope().delete(name);
      });
      if (cmd == null)
        throw new IllegalArgumentException("Command event does not include legal data");
      String action = (String)cmd.get("action");
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
              sensorDataReceived(bp, new String(m.getPayload(), StandardCharsets.UTF_8)));
          comm.consumeFromQueue(QueueNameEnum.Free, (topic, m) ->
              bp.enqueueExternalEvent(new BEvent("GetAlgorithmResult", new String(m.getPayload(), StandardCharsets.UTF_8))));
        }
        send(message.get(), cmd2queue.getOrDefault(action, QueueNameEnum.Commands));
      } catch (MqttException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void send(String message, QueueNameEnum queue) throws MqttException {
    comm.send(message, queue);
  }
}
