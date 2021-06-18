package il.ac.bgu.cs.bp.bprobot.util.Communication;

import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface ICommunication {

    void purgeQueue(QueueNameEnum queue) throws IOException;

    void consumeFromQueue(QueueNameEnum queue, DeliverCallback callback) throws IOException;

    void closeConnection() throws IOException, TimeoutException;

    void send(String message, QueueNameEnum queue) throws IOException;

    void setCredentials(String host, String username, String password);

    void connect() throws IOException, TimeoutException;
}


