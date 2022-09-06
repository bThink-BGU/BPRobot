package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi;

import lejos.hardware.port.Port;

public enum GrovePiPort implements Port {
  A0, A1, A2, D2, D3, D4, D5, D6, D7, D8;

  @Override
  public String getName() {
    return name();
  }
}
