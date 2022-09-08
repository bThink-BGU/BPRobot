package il.ac.bgu.cs.bp.bprobot.util.communication;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

import static il.ac.bgu.cs.bp.bprobot.util.communication.QueueNameEnum.*;

public class MQTTCommunication {
    private String host = "tcp://localhost:1883";
    private String username = "";
    private String password = "";
    private final String clientId = UUID.randomUUID().toString();;
    private final MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient client;

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

    /**
     * Async listen to queue and execute callback when messages from it arrive
     * @param queue name of queue to listen to
     * @param callback to execute when messages arrive
     */
    public void consumeFromQueue(QueueNameEnum queue, IMqttMessageListener callback) throws MqttException {
        client.subscribe(queue.name(), callback);
    }

    public void closeConnection() throws MqttException {
        if (client.isConnected()) {
            client.disconnect();
        }
        client.close();
    }

    public void send(String message, QueueNameEnum queueName) throws MqttException {
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(1);
        client.publish(queueName.name(), msg);
    }

    /**
     * Set credentials of the broker
     * @param host name/IP address of target machine
     * @param username to log in with
     * @param password to log in with
     */
    public void setCredentials(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }
}
