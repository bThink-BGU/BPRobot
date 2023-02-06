package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.communication;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class SerialCommunicator implements ICommunicator {
  private static final Logger logger = Logger.getLogger(SerialCommunicator.class.getName());
  private static final int TIMEOUT = 1000;

  private final SerialPort port;
  private int delay = 0;

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public SerialCommunicator(String portName) {
    logger.info("EV3.EV3 object initiated at - " + LocalDateTime.now());
    logger.config("Trying to connect to EV3.EV3 Brick...");
    SerialPort port = null;
    for (SerialPort sp : SerialPort.getCommPorts()) {
      if (sp.getSystemPortName().equals(portName)) {
        port = sp;
        logger.config("Connecting to: " + sp.getSystemPortName());
        break;
      }
    }
    if (port == null) {
      logger.severe("No EV3.EV3 Brick Found!");
      throw new NullPointerException("Brick was not found!\n");
    } else {
      this.port = port;
    }
  }

  @Override
  public void open() throws IOException {
    port.openPort();
    logger.info("Port open!");
    port.setComPortTimeouts
        (SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, TIMEOUT, TIMEOUT);
  }

  @Override
  public void close() {
    stop();
    port.closePort();
  }

  @Override
  public void write(byte[] message) throws RuntimeException {
    port.writeBytes(message, message.length);
  }

  @Override
  public byte[] read(int length) throws RuntimeException {
    byte[] buffer = new byte[length];
    int numBytes = port.readBytes(buffer, length);
    byte[] result = new byte[numBytes];
    System.arraycopy(buffer, 0, result, 0, numBytes);

    return result;
  }

  /**
   * Stop all motors at once.
   */
  public void stop() {
//    spin(0,0,0,0);
    delay();
  }

  private void delay() {
    if (delay > 0) {
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
