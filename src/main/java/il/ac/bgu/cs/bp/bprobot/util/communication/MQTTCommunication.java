package il.ac.bgu.cs.bp.bprobot.util.communication;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum.Commands;
import static il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum.SOS;
import static il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum.Data;
import static il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum.Free;

public class MQTTCommunication implements IMQTTCommunication {
    private String host = "tcp://broker.emqx.io:1883";
    private String username = "emqx_test";
    private String password = "emqx_test_password";
    private final String clientId = "emqx_test";
    private final MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient client;

    @Override
    public void connect() throws MqttException {
        client = new MqttClient(host, clientId, persistence);
        // MQTT connection option
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());
        // retain session
        connOpts.setCleanSession(true);

        // establish a connection
        client.connect(connOpts);

        client.getTopic(Commands.name());
        client.getTopic(Data.name());
        client.getTopic(SOS.name());
        client.getTopic(Free.name());
    }

    @Override
    public void consumeFromTopic(QueueNameEnum queue, IMqttMessageListener callback) throws MqttException {
        client.subscribe(queue.name(), callback);
    }

    @Override
    public void closeConnection() throws MqttException {
        if (client.isConnected()) {
            client.disconnect();
        }
        client.close();
    }

    @Override
    public void send(String message, QueueNameEnum queueName) throws MqttException {
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(1);
        client.publish(queueName.name(), msg);
    }

    @Override
    public void setCredentials(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }
}
