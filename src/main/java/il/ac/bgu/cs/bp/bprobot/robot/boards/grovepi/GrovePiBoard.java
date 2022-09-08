package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.pi4j.GrovePi4J;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.DeviceWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.util.ReflectionUtils;
import lejos.hardware.port.Port;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrovePiBoard extends Board {
  private final GrovePi grovePi;
  private static final Logger logger = Logger.getLogger(GrovePiBoard.class.getName());

  public GrovePiBoard() {
    super(List.of("il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers"));
    try {
      grovePi = new GrovePi4J();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    logger.setLevel(Level.SEVERE);
  }

  @Override
  public void putDevice(Port port, String deviceName, String type, Integer mode) {
    try {
      var dev = ReflectionUtils.<DeviceWrapper<?>>create(type, packages, grovePi);
      if(mode != null) {
        ((SensorWrapper<?>)dev).setCurrentMode(mode);
      }
      putDevice(port, deviceName, dev);
    } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Failed to create device " + deviceName, e);
    }
  }

  @Override
  public GrovePiPort getPort(String portName) {
    return GrovePiPort.valueOf(portName);
  }

  @Override
  public void close() {
    grovePi.close();
  }
}
