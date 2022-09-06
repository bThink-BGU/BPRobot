package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi;

import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.GroveDeviceWrapper;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrovePiBoard extends Board<GrovePiPort, GroveDeviceWrapper> {

//  private final GrovePi4J grove = new GrovePi4J();

  private static final Logger logger = Logger.getLogger(GrovePiBoard.class.getName());
  protected final Map<GrovePiPort, Integer> sensorModes;

  public GrovePiBoard() {
    super();
    sensorModes = Map.of(
        GrovePiPort.A0, 0,
        GrovePiPort.A1, 0,
        GrovePiPort.A2, 0,
        GrovePiPort.D2, 0,
        GrovePiPort.D3, 0,
        GrovePiPort.D4, 0,
        GrovePiPort.D5, 0,
        GrovePiPort.D6, 0,
        GrovePiPort.D7, 0,
        GrovePiPort.D8, 0
    );
    logger.setLevel(Level.SEVERE);
  }

  public GrovePiBoard(Map<GrovePiPort, GroveDeviceWrapper> devices) {
    super(devices);
    sensorModes = Map.of(
        GrovePiPort.A0, 0,
        GrovePiPort.A1, 0,
        GrovePiPort.A2, 0,
        GrovePiPort.D2, 0,
        GrovePiPort.D3, 0,
        GrovePiPort.D4, 0,
        GrovePiPort.D5, 0,
        GrovePiPort.D6, 0,
        GrovePiPort.D7, 0,
        GrovePiPort.D8, 0
    );
    logger.setLevel(Level.SEVERE);
  }
}
