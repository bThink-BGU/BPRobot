package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.pi4j.GrovePi4J;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.DeviceWrapper;

import java.io.IOException;
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
  public GrovePiPort getPort(String portName) {
    return GrovePiPort.valueOf(portName);
  }

  @Override
  public void close() {
    grovePi.close();
  }
}
