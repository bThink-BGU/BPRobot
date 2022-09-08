package il.ac.bgu.cs.bp.bprobot.actuator;

import org.eclipse.paho.client.mqttv3.MqttException;

public class DeviceActuator {
  public static void main(String[] args) throws MqttException {
    new CommandHandler().run();
  }
}
