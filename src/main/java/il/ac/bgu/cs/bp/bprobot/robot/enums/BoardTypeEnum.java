package il.ac.bgu.cs.bp.bprobot.robot.enums;

public enum BoardTypeEnum {
  EV3, GrovePi;

  // Get the board's matching port
  public IPortEnums getPortType(String port) {
    switch (valueOf(this.name())) {
      case EV3:
        return IEv3Port.getPortType(port);

      case GrovePi:
        return GrovePiPort.valueOf(port);

    }
    throw new IllegalArgumentException();
  }
}
