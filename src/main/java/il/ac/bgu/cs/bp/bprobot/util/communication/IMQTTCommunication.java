package il.ac.bgu.cs.bp.bprobot.util.communication;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface IMQTTCommunication {

//    void purgeQueue(QueueNameEnum queue);

    /**
     * Listen to queue and execute callback when messages from it arrive
     * @param queue name of queue to listen to
     * @param callback to execute when messages arrive
     */
    void subscribe(QueueNameEnum queue, IMqttMessageListener callback) throws MqttException;

    void closeConnection() throws MqttException;

    void send(String message, QueueNameEnum queue) throws MqttException;

    /**
     * Set credentials of the broker
     * @param host name/IP address of target machine
     * @param username to log in with
     * @param password to log in with
     */
    void setCredentials(String host, String username, String password);

    /**
     * Connect to RabbitMQ queues.
     * @throws MqttException
     */
    void connect() throws MqttException;
}


